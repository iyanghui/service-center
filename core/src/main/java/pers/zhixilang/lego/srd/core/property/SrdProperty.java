package pers.zhixilang.lego.srd.core.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:34
 */
@ConfigurationProperties("lego.srd")
@Data
public class SrdProperty {

    private String name;

    private Server server;

    private Client client;

    private Instance instance;

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

    @Data
    public static class Client {
        /**
         * registry server地址
         */
        private String registryUrl;

        /**
         * 从server端拉取配置时间间隔(ms)
         * 60s
         */
        private Integer fetchIntervalMs = 60000;

        /**
         * 心跳时间间隔(ms)
         * default 60s
         */
        private Integer hearBeatIntervalMs = 60000;
    }

    @Data
    public static class Instance {

        /**
         * 实例ID
         */
        private String id;

        /**
         * 应用名称
         */
        private String appName = "UNKNOW";

        /**
         * 实例地址,ip
         * 默认取
         */
        private String ip;

        /**
         * (多网卡下)选择的网卡
         */
        private String chooseEth;

        /**
         * host name
         */
        private String host;

        /**
         * (相较于hostName)优先使用ip
         */
        private Boolean preferIp = true;

        /**
         * 实例端口
         * 默认取server.port
         */
        private Integer port;

    }
}
