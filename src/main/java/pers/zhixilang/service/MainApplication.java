package pers.zhixilang.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhixilang
 * @version 1.0
 * @date 2019-10-02 9:43
 */
public class MainApplication {


    public static void main(String[] args) throws Exception{

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("route.xml");

    }

}
