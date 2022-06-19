package pers.zhixilang.lego.srd.base.util;

import java.util.UUID;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:54
 */
public final class IdUtils {
    private IdUtils() {}

    public static String getReqID() {
        return UUID.randomUUID().toString();
    }
}
