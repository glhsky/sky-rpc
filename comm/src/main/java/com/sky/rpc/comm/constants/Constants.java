package com.sky.rpc.comm.constants;

/**
 * @author bainao
 */
public class Constants {

    /**
     * 默认端口
     */
    public static final Integer DEFAULT_PORT = 52057;

    /**
     * 默认ip
     */
    public static final String DEFAULT_HOST = "127.0.0.1";

    /**
     * 默认注册地址
     */
    public static final String DEFAULT_REGISTRY = "127.0.0.1:2181";

    /**
     * 默认发现地址
     */
    public static final String DEFAULT_DISCOVER = "127.0.0.1:2181";

    /**
     * 默认权重
     */
    public static final Integer SERVER_DEFAULT_WEIGHT = 1;

    /**
     * 服务地址与权重的分隔符 127.0.0.1:52057$2
     */
    public static final String DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_SEPARATOR = "$";

    /**
     * 服务地址与权重的分隔符 127.0.0.1:52057$2
     */
    public static final String DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_CUTTING_CHARACTER = "\\$";
}
