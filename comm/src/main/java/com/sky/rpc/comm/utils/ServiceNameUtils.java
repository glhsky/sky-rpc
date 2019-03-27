package com.sky.rpc.comm.utils;

import com.google.common.base.Strings;
import com.sky.rpc.comm.model.ServerInfo;

/**
 * @author bainao
 */
public class ServiceNameUtils {

    public static String getServiceName(Class<?> interfaceClass, String version) {
        if (!Strings.isNullOrEmpty(version)) {
            return interfaceClass.getName() + "." + version;
        }
        return interfaceClass.getName();
    }

    public static String getServiceAddress(String host, int port) {
        return host + ":" + port;
    }

    public static String getServiceAddress(ServerInfo serverInfo) {
        return serverInfo.getHost() + ":" + serverInfo.getPort();
    }
}
