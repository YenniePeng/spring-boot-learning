package com.yennie.profiles;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 根据当前激活的 Profile 创建不同的 Bean。
 * 两个方法使用相同的 Bean 名称，调用方切换环境时不需要修改注入代码。
 */
@Configuration
public class ApplicationConfig {

    @Bean("profileBean")
    @Profile("dev")
    public EnvironmentMessage devMessage() {
        return new EnvironmentMessage("Development bean");
    }

    @Bean("profileBean")
    @Profile("test")
    public EnvironmentMessage testMessage() {
        return new EnvironmentMessage("Test bean");
    }
}
