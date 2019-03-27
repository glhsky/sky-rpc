package com.sky.rpc.netty.bootstrap;

import com.sky.rpc.model.Request;
import com.sky.rpc.model.Response;

/**
 * @author bainao
 */
public interface Client {

    Response sendRequest(Request request) throws InterruptedException;
}
