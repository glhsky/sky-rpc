package com.sky.rpc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author bainao
 */
@Data
public class Request implements Serializable {

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] paramValves;

    private String version;

    private long startTime;
}
