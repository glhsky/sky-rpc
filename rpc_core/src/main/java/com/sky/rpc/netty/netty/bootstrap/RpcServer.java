package com.sky.rpc.netty.netty.bootstrap;

import com.google.common.collect.Maps;
import com.sky.annotation.Provider;
import com.sky.rpc.model.model.ServerInfo;
import com.sky.rpc.netty.netty.codec.Decoder;
import com.sky.rpc.netty.netty.codec.Encoder;
import com.sky.rpc.netty.netty.handler.RpcServerHandler;
import com.sky.rpc.registry.ServiceRegistry;
import com.sky.rpc.utils.ServiceNameUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bainao
 */
public class RpcServer implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    private Map<String, Object> providerBeans = Maps.newConcurrentMap();

    private final ServiceRegistry serviceRegistry;

    private final ServerInfo serverInfo;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    public RpcServer(ServiceRegistry serviceRegistry, ServerInfo serverInfo) {
        this.serviceRegistry = serviceRegistry;
        this.serverInfo = serverInfo;
    }

    @Override
    public void start(String basePackage) throws InterruptedException {
        registerProviderBean2Map(basePackage);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new Decoder())
                                .addLast(new Encoder())
                                .addLast(new RpcServerHandler((ConcurrentHashMap<String, Object>) providerBeans));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = bootstrap.bind(serverInfo.getHost(), serverInfo.getPort()).sync();
        registerProviderService2Registry();
        LOGGER.debug("start server on port <{}>", serverInfo.getPort());
        future.channel().closeFuture().sync();
        close();
    }

    @Override
    public void close() {
        serviceRegistry.shutdown();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    private void registerProviderBean2Map(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> set = reflections.getTypesAnnotatedWith(Provider.class);
        for (Class<?> clazz : set) {
            Provider provider = clazz.getAnnotation(Provider.class);
            String version = provider.version();
            Class<?> interfaceClass = provider.interfaceClass();
            String serviceName = ServiceNameUtils.getServiceName(interfaceClass, version);
            Object bean = null;
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            providerBeans.put(serviceName, bean);
        }
    }

    private void registerProviderService2Registry() {
        String serviceAddress = ServiceNameUtils.getServiceAddress(this.serverInfo);
        for (String serviceName : providerBeans.keySet()) {
            serviceRegistry.register(serviceName, serverInfo);
        }
    }
}
