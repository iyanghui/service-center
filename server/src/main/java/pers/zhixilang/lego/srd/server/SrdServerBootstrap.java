package pers.zhixilang.lego.srd.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pers.zhixilang.lego.srd.core.property.SrdProperty;
import pers.zhixilang.lego.srd.server.cache.CacheManager;
import pers.zhixilang.lego.srd.server.netty.NettyServer;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:15
 */
@Slf4j
public class SrdServerBootstrap implements DisposableBean {

    /**
     * netty server
     */
    NettyServer nettyServer = null;

    public void run(SrdProperty property, CacheManager cacheManager) {

        SrdServerContext.init(cacheManager);

        try {
            nettyServer =  new NettyServer(property);
            nettyServer.startup();
        } catch (Exception e) {
            log.error("Application terminated, netty server bootstrap error: ", e);
            System.exit(0);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (null != nettyServer) {
            nettyServer.shutdown();
        }
    }
}
