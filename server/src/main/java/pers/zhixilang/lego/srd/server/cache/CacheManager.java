package pers.zhixilang.lego.srd.server.cache;

import pers.zhixilang.lego.srd.base.pojo.InstanceInfo;
import pers.zhixilang.lego.srd.base.pojo.Value;
import pers.zhixilang.lego.srd.server.config.SrdServerConfig;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:08
 */
public interface CacheManager {

    /**
     * 获取所有数据
     * @return 应用信息
     */
    Value getAll();

    /**
     * 获取增量数据
     * @return 应用信息
     */
    Value getIncremental();

    /**
     * 实例注册
     * @param instance 实例
     */
    void register(InstanceInfo instance);

    /**
     * 续约
     * @param instance 实例
     */
    void renew(InstanceInfo instance);

    /**
     * 实例下线
     * @param instance 实例
     */
    void down(InstanceInfo instance);

    /**
     * 超时未续约-剔除
     * @param instance 实例
     */
    void evict(InstanceInfo instance);

    /**
     * 获取server config
     * @return SrdServerConfig
     */
    SrdServerConfig getConfig();
}
