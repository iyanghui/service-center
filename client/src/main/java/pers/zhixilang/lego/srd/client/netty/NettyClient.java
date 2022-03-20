package pers.zhixilang.lego.srd.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import pers.zhixilang.lego.srd.core.codec.NettyDecoder;
import pers.zhixilang.lego.srd.core.codec.NettyEncoder;
import pers.zhixilang.lego.srd.core.property.SrdProperty;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:06
 */
@Slf4j
public class NettyClient {
    private EventLoopGroup workers;

    private Bootstrap bootstrap;

    /**
     * 重试间隔（秒）
     */
    private static final int NETTY_RETRY_PERIOD_SEC = 10;

    public void startup(SrdProperty property) throws Exception {
        workers = new NioEventLoopGroup(1);

        NettyClientHandler clientHandler = new NettyClientHandler(this);

        bootstrap = new Bootstrap();
        bootstrap.group(workers)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(0, 0,
                                property.getClient().getHearBeatIntervalMs() / 1000))
                                .addLast(new NettyDecoder())
                                .addLast(new NettyEncoder())
                                .addLast(clientHandler);
                    }
                });


        InetSocketAddress address;
        try {
            String[] arr = property.getClient().getRegistryUrl().split(":");
            address = new InetSocketAddress(arr[0], Integer.parseInt(arr[1]));
        }  catch (Exception e) {
            throw new IllegalArgumentException("property lego.srd.server.address illegal.");
        }

        this.doConnect(address);
    }

    public void shutdown() {
        if (null != workers) {
            workers.shutdownGracefully();
        }
    }

    public Channel doConnect(SocketAddress address) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(address);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    log.warn("srd netty client connect to {} fail: {}", address, future.cause().getMessage());
                    future.channel().eventLoop().schedule(() -> doConnect(address), NETTY_RETRY_PERIOD_SEC, TimeUnit.SECONDS);
                } else {
                    log.info("srd netty client connect to {} success.", address);
                }
            }
        });
        return channelFuture.sync().channel();
    }

    public void heartBeat(Channel channel) {
        log.debug("heartBeat...");
        // TODO
//        channel.write(null);
    }

    public void register(Channel channel) {
        log.debug("start register...");
        // TODO
    }
}
