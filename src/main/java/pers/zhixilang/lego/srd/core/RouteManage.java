package pers.zhixilang.lego.srd.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-14 16:30
 */
public class RouteManage {

    /**
     * 存放所有路由
     * key: path prefix
     * value: List => remote url
     */
    public static ConcurrentHashMap<String, Set<String>> routeMap;

    /**
     * 获取remote URL集合
     * @param prefix path prefix
     * @return 路由集合
     */
    public static Set<String> getRoutes(String prefix) {
        if (routeMap == null) {
            return new HashSet<>();
        }
        for (Map.Entry<String, Set<String>> entry: routeMap.entrySet()) {
            if (prefix.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }
        return new HashSet<>();
    }
}
