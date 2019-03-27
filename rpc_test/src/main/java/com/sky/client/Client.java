package com.sky.client;

import com.sky.api.IHelloService;
import com.sky.rpc.proxy.DefaultProxy;
import com.sky.rpc.registry.ServiceDiscover;
import com.sky.rpc.registry.zk.ZookeeperServiceDiscover;

/**
 * @author bainao
 */
public class Client {
    public static void main(String[] args) {
        String zkServer = "127.0.0.1:2181";
        ServiceDiscover serviceDiscover = new ZookeeperServiceDiscover(zkServer);
        DefaultProxy proxy = new DefaultProxy(serviceDiscover);

        IHelloService helloService = proxy.createProxy(IHelloService.class, "1.0.0");

        System.out.println(helloService.sayHello("bainao1,你好帅啊"));
        System.out.println(helloService.sayHello("bainao2,你好帅啊"));
        System.exit(0);
    }
}
