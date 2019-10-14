package pers.zhixilang.service.registry;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pers.zhixilang.service.bean.RouteBean;
import pers.zhixilang.service.common.NamedThreadFactory;
import pers.zhixilang.service.common.RedisUrl;
import pers.zhixilang.service.core.Constants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-02 19:51
 */
public class RedisRegistry {
    private final ScheduledExecutorService expireExecutor;

    private JedisPool jedisPool;

    private static RedisRegistry instance = null;

    private RedisUrl redisURL;

    private AtomicInteger atomicInteger = new AtomicInteger(0);

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

        Jedis jedis = jedisPool.getResource();
        while (jedis.setnx(Constants.LOCK_SERVICE_ROUTE_REGISTRY_KEY, Constants.LOCK_SERVICE_ROUTE_REGISTRY_VALUE) == 0) {
            Thread.sleep(300L);
        }

        for (RouteBean routeBean: routeBeans) {
            jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, Constants.DEFAULT_SCORE,
                    routeBean.getPrefix() + "@" + routeBean.getRoute());
        }

        jedis.del(Constants.LOCK_SERVICE_ROUTE_REGISTRY_KEY);

        long expirePeriod = redisURL.getPeriod() == null ? Constants.EXPIRE_PERIOD : redisURL.getPeriod();
        expireExecutor.scheduleWithFixedDelay(() -> registry(routeBeans), 0L, expirePeriod, TimeUnit.MILLISECONDS);
    }


    private void registry(List<RouteBean> routeBeans) {
        System.out.println("===第 " + atomicInteger.getAndIncrement() + " 次");
        Jedis jedis = jedisPool.getResource();

        for (RouteBean routeBean: routeBeans) {
            String member = routeBean.getPrefix() + "@" + routeBean.getRoute();
            Double time = jedis.zscore(Constants.KEY_REDIS_SERVICE_REGISTRY, member);
            if (time == null) {
                jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, Constants.DEFAULT_SCORE, member);
            } else {
                jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, time + redisURL.getPeriod(), member);
            }
            jedis.publish(Constants.CHANNEL_SERVICE_REGISTRY, member);
        }

    }

    private RedisRegistry(RedisUrl redisURL) {

        this.redisURL = redisURL;

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();

        // TODO 参数判断
        jedisPool = new JedisPool(config, redisURL.getHost(), redisURL.getPort(), redisURL.getTimeout(), redisURL.getPassword());

        expireExecutor = new ScheduledThreadPoolExecutor(3, new NamedThreadFactory(
                "service-registry-retry" +
                        "-expire-timer", true));
    }
}
