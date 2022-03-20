package pers.zhixilang.lego.srd.core;

import cn.hutool.json.JSONUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pers.zhixilang.lego.srd.core.enums.Event;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:08
 */
@Getter
@Setter
@Builder
public class Request {
    private String uuid;

    private Event event;

    private String body;

    public void setBodyPojo(Object o) {
        this.body = JSONUtil.toJsonStr(o);
    }

    public <T> T  getBodyPojo(Class<T> clazz) {
        return JSONUtil.toBean(this.body, clazz);
    }

    @Override
    public String toString() {
        return "Request{" +
                "uuid='" + uuid + '\'' +
                ", event=" + event +
                ", body='" + body + '\'' +
                '}';
    }
}
