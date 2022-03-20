package pers.zhixilang.lego.srd.server.annotation;

import org.springframework.context.annotation.Import;
import pers.zhixilang.lego.srd.server.SrdServerAutoConfiguration;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhixilang
 * @version 1.0.0
 * date 2022-03-16 11:02
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(SrdServerAutoConfiguration.class)
public @interface EnableSrdServer {
}
