package com.yennie.profiles;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final EnvironmentMessage environmentMessage;
    private final String greeting;

    public ProfileService(
            @Qualifier("profileBean") EnvironmentMessage environmentMessage,
            @Value("${app.greeting}") String greeting
    ) {
        this.environmentMessage = environmentMessage;
        this.greeting = greeting;
    }

    public String describeEnvironment() {
        return environmentMessage.getMessage() + " | " + greeting;
    }
}
