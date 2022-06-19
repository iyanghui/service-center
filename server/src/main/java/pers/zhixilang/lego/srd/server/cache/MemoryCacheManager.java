package pers.zhixilang.lego.srd.server.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import pers.zhixilang.lego.srd.base.pojo.InstanceInfo;
import pers.zhixilang.lego.srd.base.pojo.Key;
import pers.zhixilang.lego.srd.base.pojo.Value;
import pers.zhixilang.lego.srd.server.config.SrdServerConfig;
import pers.zhixilang.lego.srd.server.exception.ServerException;
import pers.zhixilang.lego.srd.server.pojo.Lease;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:13
 */
@Slf4j
public class MemoryCacheManager implements CacheManager {

    /**
     * 面向client的一级缓存
     */
    private Map<Key, Value> readOnlyMap = new ConcurrentHashMap<>();

    /**
     * 二级缓存
     * 一级缓存查询不到时，再从这里查询
     */
    private LoadingCache<Key, Value> readWriteMap;

    /**
     * 实时缓存
     * {@code <appName, <instanceId, Lease>>}
     */
    private Map<String, Map<String, Lease>> registry = new ConcurrentHashMap<>();

    /**
     * 存储变化数据，增量
     */
    private Set<InstanceInfo> changeSet = new CopyOnWriteArraySet<>();

    private AtomicLong globalVersion = new AtomicLong(0);

    private SrdServerConfig config;

    public MemoryCacheManager(SrdServerConfig config) {
        this.config = config;


        readWriteMap = CacheBuilder.newBuilder()
        .build(new CacheLoader<Key, Value>() {
            @Override
            public Value load(Key key) {
                // get from registry

                switch (key.getType()) {
                    case ALL:
                        break;
                    case INCREMENTAL:
                        break;
                    default: break;

                }
                return null;
            }
        });

        cacheSyncTask();
    }

    /**
     * @see CacheManager#getAll()
     */
    @Override
    public Value getAll() {
        return getValue(new Key(Key.Type.ALL));
    }

    /**
     * @see CacheManager#getIncremental()
     */
    @Override
    public Value getIncremental() {
        return getValue(new Key(Key.Type.INCREMENTAL));
    }

    /**
     * @see CacheManager#register(InstanceInfo)
     */
    @Override
    public void register(InstanceInfo instance) {
        Map<String, Lease> map = registry.get(instance.getAppName());
        if (null ==  map) {
            map = new ConcurrentHashMap<>();
            registry.put(instance.getAppName(), map);
        }

        long now = System.currentTimeMillis();
        Lease lease = new Lease();
        lease.setHolder(instance);
        lease.setRegisterTs(now);
        lease.setLastUpdateTs(now);
        lease.setEvictionTs(now + instance.getHearBeatIntervalMs() * 2);

        map.put(instance.getInstanceId(), lease);

        this.recordChange(instance);

        this.invalidate();

    }

    @Override
    public void renew(InstanceInfo instance) {

    }

    /**
     * @see CacheManager#down(InstanceInfo)
     */
    @Override
    public void down(InstanceInfo instance) {
        // registry update
        this.recordChange(instance);
    }

    private Value getValue(Key key) {
        if (null == key) {
            throw new ServerException("key is null");
        }
        Value value = readOnlyMap.get(key);
        if (null == value) {
            try {
                value = readWriteMap.get(key);
            } catch (Exception e) {
                throw new ServerException("can't get key 「"+ key +"」");
            }
        }
        return value;
    }


    private void recordChange(InstanceInfo instanceInfo) {
        changeSet.remove(instanceInfo);
        changeSet.add(instanceInfo);
    }

    private void invalidate() {
        for (Key.Type type: Key.Type.values()) {
            Key key = new Key(type);
            readWriteMap.invalidate(key);
            log.debug("invalidated readWriteMap key 「{}」", key);
        }
    }

    /**
     * 缓存同步task
     */
    private void cacheSyncTask() {
        long syncCacheMs = config.getServer().getSyncCacheIntervalMs();

        new Timer("srd-cache-update").schedule(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<Key, Value> entry: readOnlyMap.entrySet()) {
                    Key key = entry.getKey();
                    Value currentValue = entry.getValue();
                    try {
                        Value latestValue = readWriteMap.get(entry.getKey());
                        if (Objects.equals(latestValue, currentValue))  {
                            readOnlyMap.put(entry.getKey(), latestValue);
                        }
                    } catch (Exception e) {
                        log.error("sync cache key「{}」 error:", key, e);
                    }

                }
            }
        }, (System.currentTimeMillis() / syncCacheMs) * syncCacheMs + syncCacheMs, syncCacheMs);
    }

    public void evict(InstanceInfo instance) {
        // TODO
    }

    @Override
    public SrdServerConfig getConfig() {
        return this.config;
    }
}
