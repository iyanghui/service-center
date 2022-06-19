package pers.zhixilang.lego.srd.base.pojo.event;

import lombok.Data;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:30
 */
@Data
public class RegistryEvent {
    private String appName;

    private String address;
}
