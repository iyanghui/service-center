##### 简易版的服务注册与发现中心，注册中心使用redis实现。



1. 新增配置文件route.xml：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:bridge="http://bridge.glmapper.com/schema/route"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:route="http://bridge.glmapper.com/schema/route"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://bridge.glmapper.com/schema/route
       http://bridge.glmapper.com/schema/route.xsd">

        <route:route prefix="/api/bill" route="http://127.0.0.1:9898/api/bill" />
        <route:route prefix="/api/user" route="http://127.0.0.1:9898/api/user" />

        <route:registry host="127.0.0.1" port="6379" password="" timeout="1000"
                        period="3000" />
</beans>

```



2. 使配置生效。

   - 注解方式

     ```java
     @SpringBootApplication
     @ImportResource({"route.xml"})
     public class MainApplication {
         public static void main(String[] args) {
             SpringApplication application = new SpringApplication(MainApplication.class);
             application.run(args);
         }
     }
     ```

     

   - API方式

     ```java
     public class MainApplication {
         public static void main(String[] args) throws Exception{
             ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("route.xml");
             }
       }
     ```





