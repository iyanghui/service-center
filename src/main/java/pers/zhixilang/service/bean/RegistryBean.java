package pers.zhixilang.service.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pers.zhixilang.service.common.RedisUrl;
import pers.zhixilang.service.registry.RedisRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-03 11:11
 */
public class RegistryBean implements InitializingBean, ApplicationContextAware {


    private ApplicationContext applicationContext;

    private String id;

    private String host;

    private Integer port;

    private String password;

    private Integer timeout;

    private Long period;


    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, RouteBean> routeBeanMap = applicationContext.getBeansOfType(RouteBean.class);

        if (routeBeanMap == null) {
            throw new IllegalStateException("route is null");
        }

        RedisUrl url = new RedisUrl();
        url.setHost(host)
                .setPort(port)
                .setPassword(password)
                .setTimeout(timeout)
                .setPeriod(period);

        RedisRegistry redisRegistry = RedisRegistry.getInstance(url);
        List<RouteBean> routeBeans = new ArrayList<>(routeBeanMap.values());
        redisRegistry.doRegistry(routeBeans);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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
