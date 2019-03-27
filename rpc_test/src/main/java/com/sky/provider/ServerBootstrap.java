package com.sky.provider;

import com.sky.constants.Constants;
import com.sky.rpc.model.model.ServerInfo;
import com.sky.rpc.netty.netty.bootstrap.RpcServer;
import com.sky.rpc.registry.ServiceRegistry;
import com.sky.rpc.registry.zk.ZookeeperServiceRegistry;

/**
 * @author bainao
 */
public class ServerBootstrap {
    public static void main(String[] args) throws Exception {
        String zkServer = "127.0.0.1:2181";
        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(zkServer);
        ServerInfo serverInfo = new ServerInfo(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT + 2
                , Constants.SERVER_DEFAULT_WEIGHT);
        RpcServer rpcServer = new RpcServer(serviceRegistry, serverInfo);
        rpcServer.start("com.sky.provider");
    }
}
