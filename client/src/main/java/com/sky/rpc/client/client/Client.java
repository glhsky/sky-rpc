package com.sky.rpc.client.client;

import com.sky.rpc.comm.model.Request;
import com.sky.rpc.comm.model.Response;

/**
 * @author bainao
 */
public interface Client {

    Response sendRequest(Request request) throws InterruptedException;
}
