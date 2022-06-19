package pers.zhixilang.lego.srd.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-06-19 18:34
 */
@ConfigurationProperties("lego.srd")
@Data
public class SrdClientConfig {

    /**
     * 客户端配置
     */
    private Client client;

    /**
     * 实例配置
     */
    private Instance instance;

    @Data
    public static class Client {
        /**
         * registry server地址
         */
        private String serviceUrl;

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
