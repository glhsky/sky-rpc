package com.sky.provider;

import com.sky.rpc.comm.constants.Constants;
import com.sky.rpc.comm.model.ServerInfo;
import com.sky.rpc.server.RpcServer;
import com.sky.rpc.server.ServiceRegistry;
import com.sky.rpc.server.ZookeeperServiceRegistry;

/**
 * @author bainao
 */
public class ServerBootstrap {
    public static void main(String[] args) throws Exception {
        String zkServer = "127.0.0.1:2181";
        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(zkServer);
        ServerInfo serverInfo = new ServerInfo(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT + 888
                , Constants.SERVER_DEFAULT_WEIGHT);
        RpcServer rpcServer = new RpcServer(serviceRegistry, serverInfo);
        rpcServer.start("com.sky.provider");
    }
}
