package pers.zhixilang.service.bean;

import org.springframework.beans.factory.InitializingBean;
import pers.zhixilang.service.common.RedisUrl;
import pers.zhixilang.service.discover.RedisDiscover;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-03 11:11
 */
public class DiscoverBean implements InitializingBean {

    private String id;

    private String host;

    private Integer port;

    private String password;

    private Integer timeout;

    private Long period;

    @Override
    public void afterPropertiesSet() throws Exception {
        RedisUrl url = new RedisUrl();
        url.setHost(host)
                .setPort(port)
                .setPassword(password)
                .setTimeout(timeout)
                .setPeriod(period);
        RedisDiscover redisDiscover = RedisDiscover.getInstance();
        redisDiscover.doDiscover(url);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }
}
