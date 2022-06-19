package pers.zhixilang.lego.srd.client.core;

import pers.zhixilang.lego.srd.base.pojo.InstanceInfo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-06-19 18:57
 */
public final class SrdClientContext {

    /**
     * <appName, List<Instance>>
     */
    private static ConcurrentHashMap<String, List<InstanceInfo>> serviceMap = new ConcurrentHashMap<>();

    public static List<InstanceInfo> getInstances() {
        return null;
    }

    public static InstanceInfo getInstance(String appName) {
        return null;
    }

    public static void refresh() {

    }


}
