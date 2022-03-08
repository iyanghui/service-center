package pers.zhixilang.lego.srd.core;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import pers.zhixilang.lego.srd.bean.DiscoverBean;
import pers.zhixilang.lego.srd.bean.RegistryBean;
import pers.zhixilang.lego.srd.bean.RouteBean;

/**
 * 自定义schema解析器
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-02 11:46
 */
public class RouteNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("route", new RouteBeanDefinitionParser(RouteBean.class));

        registerBeanDefinitionParser("registry", new RouteBeanDefinitionParser(RegistryBean.class));

        registerBeanDefinitionParser("discover", new RouteBeanDefinitionParser(DiscoverBean.class));
    }
}
