package pers.zhixilang.lego.srd.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import pers.zhixilang.lego.srd.client.netty.NettyClient;
import pers.zhixilang.lego.srd.core.property.SrdProperty;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:15
 */
@Slf4j
public class ClientBootstrap implements DisposableBean {

    private NettyClient nettyClient;

    public void run(SrdProperty property) {
        nettyClient = new NettyClient();
        try {
            nettyClient.startup(property);
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
