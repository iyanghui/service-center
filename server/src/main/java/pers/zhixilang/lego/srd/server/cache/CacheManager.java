package pers.zhixilang.lego.srd.server.cache;

import pers.zhixilang.lego.srd.core.pojo.InstanceInfo;
import pers.zhixilang.lego.srd.core.pojo.Value;

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
     *
     * @param instance 实例
     */
    void evict(InstanceInfo instance);
}
