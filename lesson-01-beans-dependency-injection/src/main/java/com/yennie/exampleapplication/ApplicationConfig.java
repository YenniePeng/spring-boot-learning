package com.yennie.exampleapplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 集中声明需要交给 Spring 容器管理的对象。
 *
 * <p>{@link MyFirstClass} 是普通 Java 类，没有使用 {@code @Component}，
 * 因此这里通过 {@code @Bean} 方法创建它的三个实例。</p>
 */
@Configuration
public class ApplicationConfig {

    /**
     * 显式指定 Bean 名称为 {@code myBean}。
     */
    @Bean("myBean")
    public MyFirstClass myFirstClass() {
        return new MyFirstClass("First bean");
    }

    /**
     * 未显式指定名称时，方法名 {@code mySecondBean} 就是 Bean 名称。
     */
    @Bean
    public MyFirstClass mySecondBean() {
        return new MyFirstClass("Second bean");
    }

    /**
     * 容器中存在多个相同类型的 Bean 时，{@code @Primary} 标记默认候选者。
     * 注入点如果使用 {@code @Qualifier}，则 Qualifier 的明确选择优先。
     */
    @Bean
    @Primary
    public MyFirstClass myThirdBean() {
        return new MyFirstClass("Third bean");
    }

}
