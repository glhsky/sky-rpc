package com.sky.rpc.client.proxy;

import com.google.common.base.Strings;
import com.sky.rpc.client.ServiceDiscover;
import com.sky.rpc.client.client.Client;
import com.sky.rpc.client.client.RpcClient;
import com.sky.rpc.comm.exception.HandleRpcRequestException;
import com.sky.rpc.comm.exception.NotFoundServiceInRegistryException;
import com.sky.rpc.comm.model.Request;
import com.sky.rpc.comm.model.Response;
import com.sky.rpc.comm.model.builder.RequestBuilder;
import com.sky.rpc.comm.utils.ServiceNameUtils;
import com.sky.rpc.comm.exception.NotFoundRegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * @author bainao
 */
public class DefaultProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProxy.class);

    private final ServiceDiscover serviceDiscover;

    public DefaultProxy(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    @Override
    public <T> T createProxy(Class<T> interfaceClass, String version) {
        Object obj = java.lang.reflect.Proxy.newProxyInstance(interfaceClass.getClassLoader()
                , new Class<?>[]{interfaceClass}, (Object proxy, Method method, Object[] args) -> {
                    RequestBuilder builder = new RequestBuilder();
                    builder.requestId(UUID.randomUUID().toString())
                            .interfaceName(interfaceClass.getName())
                            .methodName(method.getName())
                            .startTime(System.currentTimeMillis())
                            .paramTypes(method.getParameterTypes())
                            .paramValves(args);
                    if (!Strings.isNullOrEmpty(version)) {
                        builder.version(version);
                    }
                    Request request = builder.build();
                    if (!Objects.isNull(serviceDiscover)) {
                        String serviceName = ServiceNameUtils.getServiceName(interfaceClass, version);

                        String serverAddress = serviceDiscover.discover(serviceName);
                        if (Strings.isNullOrEmpty(serverAddress)) {
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
                    throw new NotFoundRegistryException("not found zookeeper registry!");
                });
        return (T) obj;
    }

    @Override
    public <T> T createProxy(Class<T> interfaceClass) {
        return createProxy(interfaceClass, null);
    }
}
