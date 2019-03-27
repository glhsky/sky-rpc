package com.sky.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bainao
 */
public interface ServiceDiscover {

    //<serverName,<serverAddress,weight>>
    Map<String,Map<String,Integer>> serverMap = new ConcurrentHashMap<>();

    /**
     * @param serviceName service name
     * @return service address
     */
    String discover(String serviceName) throws Exception;
}
