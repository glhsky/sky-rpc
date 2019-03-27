package com.sky.rpc.netty.netty.codec;

import com.sky.rpc.serializer.KryoSerializer;
import com.sky.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author bainao
 */
public class Encoder extends MessageToByteEncoder<Object> {

    private Serializer serializer = KryoSerializer.getSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] response = serializer.serialize(msg);
        int length = response.length;
        out.writeInt(length);
        out.writeBytes(response);
    }
}
