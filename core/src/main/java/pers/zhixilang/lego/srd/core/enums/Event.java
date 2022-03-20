package pers.zhixilang.lego.srd.core.enums;

/**
 * 消息command支持
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:08
 */
public enum Event {
    HEART_BEAT("心跳续约"),
    /**
     * server=> readWriteMap，version=latest version
     * latest version单独定时递增
     */
    REGISTRY("实例注册"),
    ALL("全量请求"),
    /**
     * client send current version
     * server return latest version
     */
    INCREMENTAL("增量请求"),

    ;

    private String remark;

    Event(String remark) {
        this.remark = remark;
    }
}
