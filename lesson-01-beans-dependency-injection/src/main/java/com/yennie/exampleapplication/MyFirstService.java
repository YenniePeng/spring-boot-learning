package com.yennie.exampleapplication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 演示构造器注入、Qualifier 选择同类型 Bean，以及 @Value 读取配置值。
 */
@Service
@PropertySources({
        @PropertySource("classpath:custom.properties"),
        @PropertySource("classpath:custom-file-2.properties")
})
public class MyFirstService {

    private final MyFirstClass myFirstClass;

    // ${...} 表示读取配置项；Spring 会把读取到的字符串转换成字段需要的类型。
    @Value("${my.custom.property}")
    private String literalMessage;

    @Value("${my.custom.property.int}")
    private Integer literalNumber;

    // 下面两个值来自 @PropertySource 加载的自定义配置文件。
    @Value("${app.greeting.morning}")
    private String morningGreeting;

    @Value("${app.greeting.afternoon}")
    private String afternoonGreeting;

    /**
     * 容器里有三个 MyFirstClass Bean，因此用 Qualifier 明确选择 myBean。
     * 单构造器会被 Spring 自动用于注入，无需再添加 @Autowired。
     */
    public MyFirstService(@Qualifier("myBean") MyFirstClass myFirstClass) {
        this.myFirstClass = myFirstClass;
    }

    public String tellAStory() {
        return "The dependency is saying: " + myFirstClass.sayHello();
    }

    public String getLiteralMessage() {
        return literalMessage;
    }

    public Integer getLiteralNumber() {
        return literalNumber;
    }

    public String getMorningGreeting() {
        return morningGreeting;
    }

    public String getAfternoonGreeting() {
        return afternoonGreeting;
    }
}
