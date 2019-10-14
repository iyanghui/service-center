package pers.zhixilang.service.discover;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pers.zhixilang.service.common.NamedThreadFactory;
import pers.zhixilang.service.common.RedisUrl;
import pers.zhixilang.service.core.Constants;
import pers.zhixilang.service.core.RouteManage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-03 11:41
 */
public class RedisDiscover {

    private ScheduledExecutorService discoverExecutor;

    private JedisPool jedisPool;

    private long period;

    private static RedisDiscover instance;

    public static RedisDiscover getInstance() {
        if (instance == null) {
            synchronized (RedisDiscover.class) {
                if (instance == null) {
                    instance = new RedisDiscover();
                }
            }
        }
        return instance;
    }


    public void doDiscover(RedisUrl redisURL) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(20);

        // TODO 参数判断
        jedisPool = new JedisPool(config, redisURL.getHost(), redisURL.getPort(), redisURL.getTimeout(), redisURL.getPassword());

        discoverExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("service-discover" +
                "-retry" +
                "-timer", true));

        this.period = redisURL.getPeriod() == null ? Constants.EXPIRE_PERIOD : redisURL.getPeriod();

        discoverExecutor.scheduleWithFixedDelay(this::discover, 0, period, TimeUnit.MILLISECONDS);
        subscribe();
    }

    private void subscribe() {
        Jedis jedis = jedisPool.getResource();
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                heartBeat(message);
            }
        }, Constants.CHANNEL_SERVICE_REGISTRY);
    }

    private void heartBeat(String url) {
        Jedis jedis = jedisPool.getResource();
        // 更新score
        double score = jedis.zscore(Constants.KEY_REDIS_SERVICE_REGISTRY, url);
        if (Constants.DEFAULT_SCORE == score) {
            // 以upstream机器时间为准
            jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, System.currentTimeMillis(), url);
        } else {
            jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, score + period, url);
        }
        jedis.close();
    }

    private void discover() {
        Jedis jedis = jedisPool.getResource();
        Set<String> urls = jedis.zrangeByScore(Constants.KEY_REDIS_SERVICE_REGISTRY,
                System.currentTimeMillis() - 2 * period, System.currentTimeMillis());
        RouteManage.routeMap = routeMapInit(urls);
        jedis.close();
    }

    private Map<String, Set<String>> routeMapInit(Set<String> urls) {
        if (urls == null || urls.size() == 0) {
            System.out.println("WARM: services is null!");
            return new HashMap<>(0);
        }
        Map<String, Set<String>> routeMap = new HashMap<>(urls.size());
        for (String url: urls) {
            String[] arr = url.split("@");
            Set<String> routes = routeMap.get(arr[0]);
            if (routes == null) {
                routes = new HashSet<>();
            }
            routes.add(arr[1]);
            routeMap.put(arr[0], routes);
        }
        return routeMap;
    }

    private RedisDiscover() {
    }

}
