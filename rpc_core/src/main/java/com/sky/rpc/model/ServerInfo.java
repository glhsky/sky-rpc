package com.sky.rpc.model;

import lombok.Data;

/**
 * @author bainao
 */
@Data
public class ServerInfo {

    private String host;

    private int port;

    private int weight;

    public ServerInfo(String host, int port, int weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }
}
