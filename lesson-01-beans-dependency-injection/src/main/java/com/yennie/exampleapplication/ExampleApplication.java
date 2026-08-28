package com.yennie.exampleapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        // run() 启动应用并返回 Spring 容器（ApplicationContext）。
        var ctx = SpringApplication.run(ExampleApplication.class, args);

        // 为了观察示例结果，这里主动从容器取出 Service。
        // 在普通业务代码中，应优先使用构造器注入，而不是到处调用 getBean()。
        MyFirstService myFirstService = ctx.getBean(MyFirstService.class);

        System.out.println(myFirstService.tellAStory());
        System.out.println(myFirstService.getLiteralMessage());
        System.out.println(myFirstService.getLiteralNumber());
        System.out.println(myFirstService.getMorningGreeting());
        System.out.println(myFirstService.getAfternoonGreeting());
    }
}
