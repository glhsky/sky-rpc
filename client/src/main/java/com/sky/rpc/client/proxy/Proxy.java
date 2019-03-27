package com.sky.rpc.client.proxy;

/**
 * @author bainao
 */
public interface Proxy {

    <T> T createProxy(final Class<T> interfaceClass, final String version);

    <T> T createProxy(final Class<T> interfaceClass);
}
