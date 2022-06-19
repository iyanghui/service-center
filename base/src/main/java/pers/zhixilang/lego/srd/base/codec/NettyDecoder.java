package pers.zhixilang.lego.srd.base.codec;

import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * netty自定义解码实现
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:21
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder {
    // TODO 测试消息长度超过65535,超过Math(2, 4*8)
    public NettyDecoder() {
        super(65535, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf decode = (ByteBuf) super.decode(ctx, in);
        if (decode == null){
            return null;
        }
        int dataLen = decode.readableBytes();
        byte[] bytes = new byte[dataLen];
        decode.readBytes(bytes);

        return JSONUtil.parse(bytes);
    }
}
