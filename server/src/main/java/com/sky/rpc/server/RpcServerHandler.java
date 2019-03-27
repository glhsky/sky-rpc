package com.sky.rpc.server;

import com.sky.rpc.comm.model.Request;
import com.sky.rpc.comm.model.Response;
import com.sky.rpc.comm.model.builder.ResponseBuilder;
import com.sky.rpc.comm.utils.ServiceNameUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bainao
 */
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);

    private final ConcurrentHashMap<String, Object> providersBean;

    public RpcServerHandler(ConcurrentHashMap<String, Object> providersBean) {
        this.providersBean = providersBean;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        String methodName = request.getMethodName();
        Object[] paramValues = request.getParamValves();
        Class<?>[] paramTypes = request.getParamTypes();
        String requestId = request.getRequestId();
        String interfaceClass = request.getInterfaceName();
        String version = request.getVersion();
        long startTime = request.getStartTime();

        Object providerBean = providersBean.get(ServiceNameUtils.getServiceName(Class.forName(interfaceClass),
                version));
        Method method = providerBean.getClass().getMethod(methodName, paramTypes);
        method.setAccessible(true);
        try {
            // reflect invoke this method
            Object result = method.invoke(providerBean, paramValues);
            ResponseBuilder builder = new ResponseBuilder();
            Response response = builder
                    .requestId(requestId)
                    .result(result)
                    .requestTime(System.currentTimeMillis() - startTime)
                    .errorCode(0)
                    .build();
            ctx.pipeline().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            ResponseBuilder builder = new ResponseBuilder();
            Response response = builder
                    .requestId(requestId)
                    .result(null)
                    .requestTime(System.currentTimeMillis() - startTime)
                    .errorCode(1)
                    .errorMsg(e.getMessage())
                    .build();
            LOGGER.error("handle remote request {} failed! {}", request, e);
            ctx.pipeline().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("exception on: {}", ctx.channel(), cause);
        ctx.channel().close();
        ctx.close();
    }
}
