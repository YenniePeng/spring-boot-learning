package com.yennie.exampleapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExampleApplicationTests {

    private final MyFirstClass primaryBean;
    private final MyFirstClass namedBean;
    private final MyFirstService myFirstService;

    @Autowired
    ExampleApplicationTests(
            MyFirstClass primaryBean,
            @Qualifier("myBean") MyFirstClass namedBean,
            MyFirstService myFirstService
    ) {
        this.primaryBean = primaryBean;
        this.namedBean = namedBean;
        this.myFirstService = myFirstService;
    }

    @Test
    void contextLoads() {
        assertThat(primaryBean.sayHello()).contains("Third bean");
        assertThat(namedBean.sayHello()).contains("First bean");
        assertThat(myFirstService.tellAStory()).contains("First bean");
        assertThat(myFirstService.getMorningGreeting()).isEqualTo("Good Morning Everyone!");
        assertThat(myFirstService.getAfternoonGreeting()).isEqualTo("Good Afternoon Everyone!");
    }
}
