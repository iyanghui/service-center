package pers.zhixilang.service.discover;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import pers.zhixilang.service.common.NamedThreadFactory;
import pers.zhixilang.service.common.RedisUrl;
import pers.zhixilang.service.core.Constants;
import pers.zhixilang.service.core.RouteManage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-03 11:41
 */
public class RedisDiscover {

    private static final Logger LOGGER = Logger.getLogger(RedisDiscover.class.getName());

    /**
     * 线程池 服务发现
     */
    private ScheduledExecutorService discoverExecutor;

    /**
     * 线程池 心跳订阅
     */
    private ExecutorService subscribeExecutor;

    /**
     * jedis池
     */
    private JedisPool jedisPool;

    /**
     * 发现周期
     */
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

        jedisPool = new JedisPool(config, redisURL.getHost(), redisURL.getPort(), redisURL.getTimeout(), redisURL.getPassword());

        discoverExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("service-discover" +
                "-retry" +
                "-timer", true));

        subscribeExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                new NamedThreadFactory(
                "service" +
                "-subscribe" + "-timer",
                true));

        this.period = redisURL.getPeriod() == null ? Constants.EXPIRE_PERIOD : redisURL.getPeriod();

        discoverExecutor.scheduleWithFixedDelay(this::discover, 0, period, TimeUnit.MILLISECONDS);
        subscribeExecutor.execute(this::subscribe);
    }

    private void subscribe() {
        Jedis jedis = jedisPool.getResource();
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                updateScore(message);
            }
        }, Constants.CHANNEL_SERVICE_REGISTRY);
    }

    private void updateScore(String url) {
        Jedis jedis = jedisPool.getResource();
        // 以upstream机器时间为准
        jedis.zadd(Constants.KEY_REDIS_SERVICE_REGISTRY, System.currentTimeMillis(), url);

        jedis.close();
//        LOGGER.info("update service [{" + url + "}] score...");
    }

    private void discover() {
        Jedis jedis = jedisPool.getResource();
        Set<String> urls = jedis.zrangeByScore(Constants.KEY_REDIS_SERVICE_REGISTRY,
                System.currentTimeMillis() - 3 * period, System.currentTimeMillis() + period);
        RouteManage.routeMap = routeMapInit(urls);
        jedis.close();
    }

    private ConcurrentHashMap<String, Set<String>> routeMapInit(Set<String> urls) {
        if (urls == null || urls.size() == 0) {
            LOGGER.warning("services is null!");
            return new ConcurrentHashMap<>(0);
        }
        ConcurrentHashMap<String, Set<String>> routeMap = new ConcurrentHashMap<>(urls.size());
        for (String url: urls) {
            String[] arr = url.split(Constants.SEPARATOR_ROUTE_URL);
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
