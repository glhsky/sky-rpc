package com.sky.rpc.comm.utils;

import com.google.common.base.Strings;
import com.sky.rpc.comm.constants.Constants;
import com.sky.rpc.comm.model.ServerInfo;

/**
 * @author bainao
 */
public class RegistryUtils {

    private static final String ZOOKEEPER_BASE_PATH = "/sky-rpc";

    private static final String SERVICES = "services";

    private static final String SERVER = "server-";

    public static String getRemoteServicePath(String serviceName) {
        if (Strings.isNullOrEmpty(serviceName)) {
            return null;
        }
        // /sky-rpc/services/'serviceName'
        return getServiceParentPath() + "/" + serviceName;
    }

    public static String getServiceParentPath() {
        // /sky-rpc/services
        return ZOOKEEPER_BASE_PATH + "/" + SERVICES;
    }

    public static String getServerPath(String serviceName) {
        if (Strings.isNullOrEmpty(serviceName)) {
            return null;
        }
        // /sky-rpc/services/'serviceName'/server-'number'
        return getRemoteServicePath(serviceName) + "/" + SERVER;
    }

    public static String getServerNodeData(ServerInfo serverInfo) {
        return serverInfo.getHost() + ":" + serverInfo.getPort()
                + Constants.DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_SEPARATOR + serverInfo.getWeight();
    }
}
