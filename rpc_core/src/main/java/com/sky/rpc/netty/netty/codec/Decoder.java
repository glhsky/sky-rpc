package com.sky.rpc.netty.netty.codec;

import com.sky.rpc.serializer.KryoSerializer;
import com.sky.rpc.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author bainao
 */
public class Decoder extends LengthFieldBasedFrameDecoder {

    private static final Serializer serializer = KryoSerializer.getSerializer();

    private static final int MAX_FRAME_LENGTH = 10 * 1024 * 1024;

    private static final int LENGTH_FIELD_LENGTH = 4;

    public Decoder() {
        super(MAX_FRAME_LENGTH, 0, LENGTH_FIELD_LENGTH, 0, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf) super.decode(ctx, in);
        if (buf != null) {
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            return serializer.deserialize(bytes);
        }
        return null;
    }
}
