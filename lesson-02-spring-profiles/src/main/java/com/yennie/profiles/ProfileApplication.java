package com.yennie.profiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ProfileApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(ProfileApplication.class);

        // 没有通过命令行、环境变量或 IntelliJ 指定 Profile 时，默认使用 dev。
        // setDefaultProperties 的优先级较低，因此外部配置仍然可以覆盖它。
        app.setDefaultProperties(
                Collections.singletonMap("spring.profiles.active", "dev")
        );

        var context = app.run(args);
        ProfileService service = context.getBean(ProfileService.class);

        System.out.println(service.describeEnvironment());
    }
}
