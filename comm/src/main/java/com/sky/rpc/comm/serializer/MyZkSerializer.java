package com.sky.rpc.comm.serializer;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

import java.nio.charset.Charset;

/**
 * @Auther: bynow
 * @Date: 2019/3/27 14:21
 * @Description:
 */
public class MyZkSerializer implements ZkSerializer {
    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        return String.valueOf(o).getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        return new String(bytes, Charset.forName("UTF-8"));
    }
}
