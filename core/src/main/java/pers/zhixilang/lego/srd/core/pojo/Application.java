package pers.zhixilang.lego.srd.core.pojo;

import lombok.Data;

import java.util.Set;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-19 20:56
 */
@Data
public class Application {
    private String appName;

    private Set<InstanceInfo> instances;
}
