package com.yennie.exampleapplication;

/**
 * 一个不依赖 Spring 注解的普通 Java 类。
 * 它的实例由 {@link ApplicationConfig} 中的 {@code @Bean} 方法创建。
 */
public class MyFirstClass {

    private final String myVar;

    /**
     * 构造器确保对象创建时就拥有必需的值。
     */
    public MyFirstClass(String myVar) {
        this.myVar = myVar;
    }

    public String sayHello() {
        return "Hello World ==> myVar = " + myVar;
    }
}
