package com.sky.rpc.registry;

import com.sky.rpc.model.model.ServerInfo;

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
