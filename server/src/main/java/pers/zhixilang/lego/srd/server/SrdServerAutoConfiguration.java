package pers.zhixilang.lego.srd.server;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.zhixilang.lego.srd.server.cache.CacheManager;
import pers.zhixilang.lego.srd.server.cache.MemoryCacheManager;
import pers.zhixilang.lego.srd.server.config.SrdServerConfig;
import pers.zhixilang.lego.srd.server.core.SrdServerBootstrap;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:02
 */
@Configuration
@EnableConfigurationProperties({SrdServerConfig.class})
public class SrdServerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public MemoryCacheManager memoryCacheManager(SrdServerConfig config) {
        return new MemoryCacheManager(config);
    }

    @Bean
    public SrdServerBootstrap serverBootstrap(CacheManager cacheManager) {
        SrdServerBootstrap serverBootstrap = new SrdServerBootstrap();
        serverBootstrap.run(cacheManager);
        return serverBootstrap;
    }

}
