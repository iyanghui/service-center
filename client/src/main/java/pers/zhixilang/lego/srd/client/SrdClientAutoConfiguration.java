package pers.zhixilang.lego.srd.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.zhixilang.lego.srd.client.config.SrdClientConfig;
import pers.zhixilang.lego.srd.client.core.ClientBootstrap;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:04
 */
@Configuration
@EnableConfigurationProperties({SrdClientConfig.class})
public class SrdClientAutoConfiguration {

    @Bean
    public ClientBootstrap clientBootstrap(SrdClientConfig config) {
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        clientBootstrap.run(config);
        return clientBootstrap;
    }
}
