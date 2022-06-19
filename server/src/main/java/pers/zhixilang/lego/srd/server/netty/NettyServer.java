package pers.zhixilang.lego.srd.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import pers.zhixilang.lego.srd.base.codec.NettyDecoder;
import pers.zhixilang.lego.srd.base.codec.NettyEncoder;
import pers.zhixilang.lego.srd.server.config.SrdServerConfig;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:07
 */
@Slf4j
public class NettyServer {

    private SrdServerConfig config;

    private EventLoopGroup masters = null;

    private EventLoopGroup workers = null;

    public NettyServer(SrdServerConfig config) {
        this.config = config;
    }

    public void startup() throws Exception {
        masters = new NioEventLoopGroup(1);
        workers = new NioEventLoopGroup(10);

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masters, workers)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(0, 0,
                                    config.getServer().getEvictionIntervalMs() / 1000))
                                    .addLast(new NettyEncoder())
                                    .addLast(new NettyDecoder())
                                    .addLast(new NettyServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture future = serverBootstrap.bind(config.getServer().getPort())
                    .sync();

            log.info("srd netty server starting...");

            future.channel()
                    .closeFuture()
                    .sync();
        } finally {
            masters.shutdownGracefully();
            workers.shutdownGracefully();
        }
    }

    public void shutdown() {
        if (null != masters) {
            masters.shutdownGracefully();
        }
        if (null != workers) {
            workers.shutdownGracefully();
        }
    }
}
