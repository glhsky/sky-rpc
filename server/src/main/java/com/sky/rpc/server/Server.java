package com.sky.rpc.server;

/**
 * @author bainao
 */
public interface Server {

    void start(String basePackage) throws InterruptedException;

    void close();
}
