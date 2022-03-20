package pers.zhixilang.lego.srd.client;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.zhixilang.lego.srd.client.cache.InstanceManager;
import pers.zhixilang.lego.srd.core.property.SrdProperty;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:04
 */
@Configuration
@EnableConfigurationProperties({SrdProperty.class})
public class SrdClientAutoConfiguration {

    @Bean
    public InstanceManager instanceManager(SrdProperty property) {
        return new InstanceManager(property);
    }

    @Bean
    public ClientBootstrap clientBootstrap(SrdProperty property) {
        ClientBootstrap clientBootstrap = new ClientBootstrap();
        clientBootstrap.run(property);
        return clientBootstrap;
    }
}
