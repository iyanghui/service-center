package pers.zhixilang.lego.srd.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:34
 */
@ConfigurationProperties("lego.srd")
@Data
public class SrdServerConfig {

    /**
     * 服务端p配置
     */
    private Server server;

    @Data
    public static class Server {

        /**
         * 监听端口，as netty server
         */
        private Integer port;

        /**
         * 是否开启自我保护
         */
        private Boolean enableSelfPreservation = false;

        /**
         * 清除无效服务实例间隔(ms)
         * default 120s
         */
        private Integer evictionIntervalMs = 120000;

        /**
         * 缓存同步时间间隔(ms)
         * default 30s
         */
        private Integer syncCacheIntervalMs = 30000;
    }
}
