package com.sky.rpc.comm.serializer;

/**
 * @author bainao
 */
public interface Serializer {

    byte[] serialize(Object data);

    <T> T deserialize(byte[] dataBytes);
}
