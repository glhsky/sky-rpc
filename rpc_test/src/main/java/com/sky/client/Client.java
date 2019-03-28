package com.sky.client;

import com.sky.api.IHelloService;
import com.sky.rpc.client.ServiceDiscover;
import com.sky.rpc.client.ZookeeperServiceDiscover;
import com.sky.rpc.client.proxy.DefaultProxy;

import java.util.Map;


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
        //System.exit(0);
        //测试一下,节点更新
        while (true) {
            Map<String, Map<String, Integer>> map = ServiceDiscover.serverMap;
            for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
                for (Map.Entry<String, Integer> entry1 : entry.getValue().entrySet()) {
                    System.out.println(entry1.getKey());
                    System.out.println(entry1.getValue());
                }
            }
            System.out.println("==========================");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
