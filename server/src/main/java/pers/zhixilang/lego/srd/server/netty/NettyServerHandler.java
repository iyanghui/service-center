package pers.zhixilang.lego.srd.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import pers.zhixilang.lego.srd.base.Request;
import pers.zhixilang.lego.srd.base.pojo.InstanceInfo;
import pers.zhixilang.lego.srd.server.core.SrdServerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:07
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * {@code <channelId, instanceInfo>}
     */
    private Map<ChannelId, InstanceInfo> channelInstanceMap = new HashMap<>();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SrdServerContext.cacheManager().down(channelInstanceMap.get(ctx.channel().id()));

        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        if (null == request.getEvent()) {
            return;
        }
        log.debug("receive client msg=>{}", request);

        switch (request.getEvent())  {
            case REGISTRY:
                InstanceInfo instanceInfo = request.getBodyPojo(InstanceInfo.class);
                SrdServerContext.cacheManager().register(instanceInfo);
                this.channelInstanceMap.put(ctx.channel().id(), instanceInfo);
                break;
            case HEART_BEAT:
                SrdServerContext.cacheManager().renew(channelInstanceMap.get(ctx.channel().id()));
                break;
            case ALL:
                break;
            case INCREMENTAL:
                break;
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            InstanceInfo instanceInfo = channelInstanceMap.get(ctx.channel().id());
            log.debug("idle event trigge, client 「{}」", instanceInfo);

            SrdServerContext.cacheManager().evict(instanceInfo);
        }
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
