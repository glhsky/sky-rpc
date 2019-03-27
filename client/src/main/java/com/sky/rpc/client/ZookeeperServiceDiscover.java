package com.sky.rpc.client;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.sky.rpc.comm.constants.Constants;
import com.sky.rpc.comm.exception.NotFoundAvailableServerInRegistry;
import com.sky.rpc.comm.exception.NotFoundServiceInRegistryException;
import com.sky.rpc.comm.serializer.MyZkSerializer;
import com.sky.rpc.comm.utils.RegistryUtils;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author bainao
 */
public class ZookeeperServiceDiscover implements ServiceDiscover {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceDiscover.class);

    private String zkServer;

    private static final int ZK_SESSION_TIMEOUT = 5000;

    private static final int ZK_CONNECTION_TIMEOUT = 1000;

    public ZookeeperServiceDiscover(String zkServer) {
        this.zkServer = zkServer;
    }

    @Override
    public String discover(String serviceName) throws Exception {

        if (!serverMap.containsKey(serviceName)) {
            ZkClient zkClient = new ZkClient(zkServer, ZK_SESSION_TIMEOUT, ZK_CONNECTION_TIMEOUT);
            zkClient.setZkSerializer(new MyZkSerializer());
            String servicePath = RegistryUtils.getRemoteServicePath(serviceName);
            if (Strings.isNullOrEmpty(servicePath) || !zkClient.exists(servicePath)) {
                throw new NotFoundServiceInRegistryException(String
                        .format("没有找到service <%s> in zookeeper registry <%s>", serviceName, zkServer));
            }
            List<String> serverNames = zkClient.getChildren(servicePath);
            if (Objects.isNull(serverNames) || serverNames.isEmpty()) {
                throw new NotFoundAvailableServerInRegistry(String
                        .format("没有找到可用的服务 service <%s> in zookeeper registry <%s>"
                                , serviceName, zkServer));
            }
            /** serverName as server-0000000001, server-000000002
             * server-00000001 -> 107.182.180.189:52057$3, 107.182.180.189 的权重为3.
             */
            Map<String, Integer> serverWeights = Maps.newHashMap();
            for (String serverName : serverNames) {
                String data = zkClient.readData(servicePath + "/" + serverName);
                String serverAddress = data.split(Constants.DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_CUTTING_CHARACTER)[0];
                String weight = data.split(Constants.DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_CUTTING_CHARACTER)[1];
                serverWeights.put(serverAddress, Integer.parseInt(weight));
            }
            serverMap.put(serviceName, serverWeights);

            zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String s, List<String> list) throws Exception {
                    ///sky-rpc/services/com.sky.api.IHelloService.1.0.0
                    LOGGER.info("节点变更路径{}", s);
                    LOGGER.info("变更的节点{}", list);
                    String[] strs = s.split("\\/");
                    String serverName = strs[strs.length - 1];
                    if (list == null || list.size() == 0) {
                        ServiceDiscover.serverMap.remove(serverName);
                    } else {
                        Map<String, Integer> map = new HashMap<>();
                        try {
                            for (String node : list) {
                                String nodeData = zkClient.readData(s + "/" + node);
                                String[] nodeDataStrs = nodeData.split(
                                        Constants.DEFAULT_SERVER_ADDRESS_WITH_WEIGHT_CUTTING_CHARACTER);
                                map.put(nodeDataStrs[0], Integer.parseInt(nodeDataStrs[1]));
                            }
                            ServiceDiscover.serverMap.put(serverName, map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        //此处之后可以扩展多种负载均衡策略
        String serverAddress = LoadBalance.randomWeightPolicyLoadBalance(serverMap.get(serviceName));
        LOGGER.info(String.format("discover service <%s> in registry, use server <%s> to handle service"
                , serviceName, serverAddress));

        return serverAddress;
    }
}
