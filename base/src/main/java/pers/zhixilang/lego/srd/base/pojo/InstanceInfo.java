package pers.zhixilang.lego.srd.base.pojo;

import lombok.Data;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-19 20:03
 */
@Data
public class InstanceInfo {
    private String appName;

    private String instanceId;

    private String address;

    private Integer hearBeatIntervalMs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstanceInfo that = (InstanceInfo) o;

        return instanceId.equals(that.instanceId);
    }

    @Override
    public int hashCode() {
        return instanceId.hashCode();
    }
}
