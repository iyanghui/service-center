package pers.zhixilang.lego.srd.core.pojo;

import lombok.Data;

/**
 * value
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:23
 */
@Data
public class Value {

    private String payload;

    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        if (!payload.equals(value.payload)) return false;
        return version.equals(value.version);
    }

    @Override
    public int hashCode() {
        int result = payload.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }
}
