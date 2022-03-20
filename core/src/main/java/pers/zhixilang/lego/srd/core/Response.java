package pers.zhixilang.lego.srd.core;

import lombok.Data;

import java.util.Objects;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 10:08
 */
@Data
public class Response {
    private String uuid;

    private Result result;

    private String body;

    @Data
    static class Result {

        private Integer code;

        private String errMsg;

        public static Result success() {
            Result result = new Result();
            result.setCode(0);
            return result;
        }

        public boolean isSuccess() {
            return Objects.equals(0, this.code);
        }

        public boolean isFail() {
            return !this.isSuccess();
        }
    }
}
