package pers.zhixilang.lego.srd.core;

import java.util.Map;
import java.util.Random;
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
     */
    public static ConcurrentHashMap<String, Set<String>> routeMap;

    public static String getRoute(String url) {
        if (routeMap == null) {
            return "";
        }
        for (Map.Entry<String, Set<String>> entry: routeMap.entrySet()) {
            if (url.startsWith(entry.getKey())) {
                // simple load balance
                return entry.getKey() + Constants.SEPARATOR_ROUTE_URL + entry.getValue().toArray()[new Random().nextInt(entry.getValue().size())].toString();
            }
        }
        return "";
    }
}
