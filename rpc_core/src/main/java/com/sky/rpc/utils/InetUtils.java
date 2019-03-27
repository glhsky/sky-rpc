package com.sky.rpc.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author bainao
 */
public class InetUtils {

    public static String getLocalHost() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println(inetAddress.getHostAddress());
        return inetAddress.getHostAddress();
    }
}
