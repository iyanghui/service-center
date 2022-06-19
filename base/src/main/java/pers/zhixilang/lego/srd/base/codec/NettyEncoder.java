package pers.zhixilang.lego.srd.base.codec;

import cn.hutool.core.util.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * netty自定义编码实现
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:21
 */
public class NettyEncoder extends MessageToMessageEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();

        byte[] bytes = SerializeUtil.serialize(msg);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        out.add(byteBuf);
    }
}
