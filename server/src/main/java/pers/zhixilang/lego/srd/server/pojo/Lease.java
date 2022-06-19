package pers.zhixilang.lego.srd.server.pojo;

import lombok.Data;
import pers.zhixilang.lego.srd.base.pojo.InstanceInfo;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-19 20:05
 */
@Data
public class Lease {
    private InstanceInfo holder;

    private Long registerTs;

    private Long lastUpdateTs;

    private Long evictionTs;

    enum State {
        NEW,
        RENEW,
        DOWN,
        ;
    }
}
