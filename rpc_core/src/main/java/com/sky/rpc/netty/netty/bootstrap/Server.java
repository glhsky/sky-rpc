package com.sky.rpc.netty.netty.bootstrap;

/**
 * @author bainao
 */
public interface Server {

    void start(String basePackage) throws InterruptedException;

    void close();
}
