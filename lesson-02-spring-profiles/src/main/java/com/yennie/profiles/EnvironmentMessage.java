package com.yennie.profiles;

/**
 * 普通 Java 对象，由 ApplicationConfig 根据当前 Profile 创建。
 */
public class EnvironmentMessage {

    private final String message;

    public EnvironmentMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
