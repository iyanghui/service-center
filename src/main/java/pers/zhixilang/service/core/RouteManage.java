package pers.zhixilang.service.core;

import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-14 16:30
 */
public class RouteManage {
    /**
     * 存放所有路由
     */
    public static Map<String, Set<String>> routeMap;

    public static String getRoute(String prefix) {
        Set<String> routes = routeMap.get(prefix);
        if (routes == null) {
            return "";
        }
        // simple load balance
        return routes.toArray()[new Random().nextInt(routes.size())].toString();
    }
}
