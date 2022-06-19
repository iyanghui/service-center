package pers.zhixilang.lego.srd.client.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pers.zhixilang.lego.srd.client.config.SrdClientConfig;
import pers.zhixilang.lego.srd.client.netty.NettyClient;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:15
 */
@Slf4j
public class ClientBootstrap implements DisposableBean {

    private NettyClient nettyClient;

    public void run(SrdClientConfig clientConfig) {
        nettyClient = new NettyClient();
        try {
            nettyClient.startup(clientConfig);
        } catch (Exception e) {
            log.error("Srd client start error: ", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (null != nettyClient)  {
            nettyClient.shutdown();
        }
    }
}
