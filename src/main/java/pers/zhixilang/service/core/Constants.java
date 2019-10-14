package pers.zhixilang.service.core;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-03 10:34
 */
public class Constants {
    /**
     * 服务注册到redis的key
     */
    public static final String KEY_REDIS_SERVICE_REGISTRY = "service:registry";

    /**
     * 服务注册通道 订阅-消费
     */
    public static final String CHANNEL_SERVICE_REGISTRY = "/channel/service/registry";

    /**
     * 过期周期 心跳
     */
    public static final Long EXPIRE_PERIOD = 6000L;

    /**
     * 并发锁 服务注册key
     */
    public static final String LOCK_SERVICE_ROUTE_REGISTRY_KEY = "lock:service:registry";

    /**
     * 并发锁 服务注册value
     */
    public static final String LOCK_SERVICE_ROUTE_REGISTRY_VALUE = "1";

    /**
     * 并发锁 score init key
     */
    public static final String LOCK_SERVICE_SCORE_INIT = "lock:service:registry";

    /**
     * 并发锁 score value
     */
    public static final String LOCK_SERVICE_SCORE_INIT_VALUE = "1";

    /**
     * 默认score
     */
    public static final double DEFAULT_SCORE = 999999D;
}
