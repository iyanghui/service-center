package pers.zhixilang.lego.srd.server.core;

import pers.zhixilang.lego.srd.server.cache.CacheManager;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-17 20:53
 */
public final class SrdServerContext {
    private SrdServerContext() {}

    private static CacheManager cacheManager;

    public static void init(CacheManager cacheManagerIml) {
        cacheManager = cacheManagerIml;
    }

    public static CacheManager cacheManager() {
        return cacheManager;
    }
}
