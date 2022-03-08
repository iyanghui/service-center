package pers.zhixilang.lego.srd.registry;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pers.zhixilang.lego.srd.bean.RouteBean;
import pers.zhixilang.lego.srd.common.NamedThreadFactory;
import pers.zhixilang.lego.srd.common.RedisUrl;
import pers.zhixilang.lego.srd.core.Constants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-02 19:51
 */
public class RedisRegistry {

    private static final Logger LOGGER = Logger.getLogger(RedisRegistry.class.getName());

    /**
     * 线程池 服务注册
     */
    private final ScheduledExecutorService expireExecutor;

    /**
     * jedis池
     */
    private JedisPool jedisPool;

    private static RedisRegistry instance = null;

    private RedisUrl redisURL;

    /**
     * 注册路由列表
     */
    private List<RouteBean> routeBeans;

    public static RedisRegistry getInstance(RedisUrl redisURL) {
        // double lock
        if (instance == null) {
            synchronized (RedisRegistry.class) {
                if (instance == null) {
                    instance = new RedisRegistry(redisURL);
                }
            }
        }
        return instance;
    }

    public void doRegistry(List<RouteBean> routeBeans) throws Exception {

        this.routeBeans = routeBeans;

        Jedis jedis = jedisPool.getResource();
        while (jedis.setnx(Constants.LOCK_SERVICE_ROUTE_REGISTRY_KEY, Constants.LOCK_SERVICE_ROUTE_REGISTRY_VALUE) == 0) {
            Thread.sleep(300L);
        }

        for (RouteBean routeBean: routeBeans) {
            String member = routeBean.getPrefix() + Constants.SEPARATOR_ROUTE_URL + routeBean.getRoute();
            jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, Constants.DEFAULT_SCORE, member);
            LOGGER.info("service [{"+ member +"}] registry success!");
        }

        jedis.del(Constants.LOCK_SERVICE_ROUTE_REGISTRY_KEY);
        jedis.close();

        long expirePeriod = redisURL.getPeriod() == null ? Constants.EXPIRE_PERIOD : redisURL.getPeriod();

        expireExecutor.scheduleWithFixedDelay(this::hearBeat, 0L, expirePeriod, TimeUnit.MILLISECONDS);
    }

    private void hearBeat() {
        try {
            Jedis jedis = jedisPool.getResource();

            for (RouteBean routeBean: routeBeans) {

                String member = routeBean.getPrefix() + Constants.SEPARATOR_ROUTE_URL + routeBean.getRoute();
                jedis.publish(Constants.CHANNEL_SERVICE_REGISTRY, member);
//                LOGGER.info(" [{"+ member +"}] hearBeat...");
            }

            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private RedisRegistry(RedisUrl redisURL) {

        this.redisURL = redisURL;

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        jedisPool = new JedisPool(config, redisURL.getHost(), redisURL.getPort(), redisURL.getTimeout(), redisURL.getPassword());

        expireExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(
                "service-registry-retry" +
                        "-timer", true));
    }
}
