package com.yennie.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
class DevProfileTests {

    @Autowired
    private ProfileService profileService;

    @Test
    void loadsDevelopmentBeanAndProperties() {
        assertThat(profileService.describeEnvironment())
                .isEqualTo("Development bean | Good Morning from DEV!");
    }
}
