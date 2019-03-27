package com.sky.rpc.proxy;

import com.google.common.base.Strings;
import com.sky.exception.HandleRpcRequestException;
import com.sky.exception.NotFoundRegistryException;
import com.sky.exception.NotFoundServiceInRegistryException;
import com.sky.rpc.model.model.Request;
import com.sky.rpc.model.model.Response;
import com.sky.rpc.model.model.builder.RequestBuilder;
import com.sky.rpc.netty.netty.bootstrap.Client;
import com.sky.rpc.netty.netty.bootstrap.RpcClient;
import com.sky.rpc.registry.ServiceDiscover;
import com.sky.rpc.utils.ServiceNameUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * @author bainao
 */
public class CglibProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CglibProxy.class);

    private final ServiceDiscover serviceDiscover;

    public CglibProxy(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    @Override
    public <T> T createProxy(Class<T> interfaceClass, String version) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceClass);
        enhancer.setCallback(new Interceptor(serviceDiscover, interfaceClass, version));
        return (T) enhancer.create();
    }

    @Override
    public <T> T createProxy(Class<T> interfaceClass) {
        return createProxy(interfaceClass, null);
    }

    static class Interceptor implements MethodInterceptor {
        private final ServiceDiscover serviceDiscover;

        private final Class<?> interfaceClass;

        private final String version;

        private Interceptor(ServiceDiscover serviceDiscover, Class<?> interfaceClass, String version) {
            this.serviceDiscover = serviceDiscover;
            this.interfaceClass = interfaceClass;
            this.version = version;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            RequestBuilder builder = new RequestBuilder();
            builder.requestId(UUID.randomUUID().toString())
                    .interfaceName(interfaceClass.getName())
                    .methodName(method.getName())
                    .startTime(System.currentTimeMillis())
                    .paramTypes(method.getParameterTypes())
                    .paramValves(objects);
            if (!Strings.isNullOrEmpty(version)) {
                builder.version(version);
            }
            Request request = builder.build();
            if (!Objects.isNull(serviceDiscover)) {
                String serviceName = ServiceNameUtils.getServiceName(interfaceClass, version);
                String serverAddress = serviceDiscover.discover(serviceName);
                if (Strings.isNullOrEmpty(serverAddress)) {
                    LOGGER.error(String.format("not found service <%s> in registry", serviceName));
                    throw new NotFoundServiceInRegistryException(String
                            .format("not found service <%s> in registry", serviceName));
                }
                String host = serverAddress.split(":")[0];
                int port = Integer.parseInt(serverAddress.split(":")[1]);
                Client rpcClient = new RpcClient(host, port);
                Response response = rpcClient.sendRequest(request);
                if (Objects.isNull(response)) {
                    throw new HandleRpcRequestException(String.format("handle request <%s> failed!", request));
                }
                response.setRequestTime(System.currentTimeMillis() - request.getStartTime());
                return response.getResult();
            }
            LOGGER.error("not found zookeeper registry!");
            throw new NotFoundRegistryException("not found zookeeper registry!");
        }
    }
}
