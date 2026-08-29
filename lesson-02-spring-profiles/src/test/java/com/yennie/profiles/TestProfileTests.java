package com.yennie.profiles;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TestProfileTests {

    @Autowired
    private ProfileService profileService;

    @Test
    void loadsTestBeanAndProperties() {
        assertThat(profileService.describeEnvironment())
                .isEqualTo("Test bean | Good Morning from TEST!");
    }
}
