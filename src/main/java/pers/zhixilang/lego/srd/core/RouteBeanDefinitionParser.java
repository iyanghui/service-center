package pers.zhixilang.lego.srd.core;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import pers.zhixilang.lego.srd.bean.DiscoverBean;
import pers.zhixilang.lego.srd.bean.RegistryBean;
import pers.zhixilang.lego.srd.bean.RouteBean;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-02 15:42
 */
public class RouteBeanDefinitionParser implements BeanDefinitionParser {

    private Class beanClass;

    public RouteBeanDefinitionParser(Class beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return parse(element, parserContext, beanClass);
    }

    private BeanDefinition parse(Element element, ParserContext context, Class beanClass) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        // spring容器初始化bean
        // (true). 使用时DI
        beanDefinition.setLazyInit(false);

        String id = element.getAttribute("id");

        if (StringUtils.isEmpty(id)) {
            String className = beanClass.getName();
            id = className;
            if (context.getRegistry().containsBeanDefinition(id)) {
                if (DiscoverBean.class.equals(beanClass)) {
                    throw new IllegalStateException("ERROR: can only be one discover!");
                }
                for (int i = 0; context.getRegistry().containsBeanDefinition(id); id = className + i++) {
                }
            }

        }
        beanDefinition.getPropertyValues().add("id", id);

        if (RouteBean.class.equals(beanClass)) {
            if (null == element.getAttribute("prefix")) {
                throw new IllegalArgumentException("prefix不能为空");
            }
            beanDefinition.getPropertyValues().add("prefix", element.getAttribute("prefix"));
            if (null == element.getAttribute("route")) {
                throw new IllegalArgumentException("route不能为空");
            }
            beanDefinition.getPropertyValues().add("route", element.getAttribute("route"));
        } else if (RegistryBean.class.equals(beanClass) || DiscoverBean.class.equals(beanClass)) {
            beanDefinition.getPropertyValues().add("host", element.getAttribute("host"));
            beanDefinition.getPropertyValues().add("port", element.getAttribute("port"));
            beanDefinition.getPropertyValues().add("password", element.getAttribute("password"));
            beanDefinition.getPropertyValues().add("timeout", element.getAttribute("timeout"));
            beanDefinition.getPropertyValues().add("period", element.getAttribute("period"));
        } else {
            throw new IllegalArgumentException("unknow bean type:" + beanClass.getName());
        }
        context.getRegistry().registerBeanDefinition(id, beanDefinition);
        return beanDefinition;
    }
}
