package com.sky.rpc.server;

import com.sky.rpc.comm.model.ServerInfo;

/**
 * @author bainao
 */
public interface ServiceRegistry {

    /**
     * @param serviceName service name
     * @param serverInfo  server info
     */
    void register(String serviceName, ServerInfo serverInfo);

    void shutdown();
}
