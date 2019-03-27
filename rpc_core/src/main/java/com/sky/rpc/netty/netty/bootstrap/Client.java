package com.sky.rpc.netty.netty.bootstrap;

import com.sky.rpc.model.model.Request;
import com.sky.rpc.model.model.Response;

/**
 * @author bainao
 */
public interface Client {

    Response sendRequest(Request request) throws InterruptedException;
}
