package pers.zhixilang.lego.srd.base.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 16:09
 */
@Getter
@Setter
public class Key {
    private Type type;

    public Key(Type type) {
        this.type = type;
    }

    public static enum Type {
        ALL,
        INCREMENTAL,
        ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        return type == key.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
