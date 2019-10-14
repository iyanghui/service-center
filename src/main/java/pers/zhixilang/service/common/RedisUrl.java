package pers.zhixilang.service.common;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-14 14:28
 */
public class RedisUrl {

    private String host;

    private Integer port;

    private String password;

    private Integer timeout;

    private Long period;

    public String getHost() {
        return host;
    }

    public RedisUrl setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public RedisUrl setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RedisUrl setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public RedisUrl setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public Long getPeriod() {
        return period;
    }

    public RedisUrl setPeriod(Long period) {
        this.period = period;
        return this;
    }
}
