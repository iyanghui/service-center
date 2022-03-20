package pers.zhixilang.lego.srd.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.zhixilang.lego.srd.core.property.SrdProperty;
import pers.zhixilang.lego.srd.server.cache.CacheManager;
import pers.zhixilang.lego.srd.server.cache.MemoryCacheManager;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:02
 */
@Configuration
@EnableConfigurationProperties({SrdProperty.class})
public class SrdServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public MemoryCacheManager memoryCacheManager(SrdProperty srdProperty) {
        return new MemoryCacheManager(srdProperty);
    }

    @Bean
    public SrdServerBootstrap serverBootstrap(SrdProperty srdProperty, CacheManager cacheManager) {
        SrdServerBootstrap serverBootstrap = new SrdServerBootstrap();
        serverBootstrap.run(srdProperty, cacheManager);
        return serverBootstrap;
    }

}
