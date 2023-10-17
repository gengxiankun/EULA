package com.eula.component.openai.enums;

import java.io.IOException;

public enum Role {

    USER, ASSISTANT, SYSTEM;

    public String toValue() {
        switch (this) {
            case USER:
                return "user";
            case ASSISTANT:
                return "assistant";
            case SYSTEM:
                return "system";
        };
        return null;
    }

    public static Role forValue(String value) throws IOException {
        if (value.equals("user")) return USER;
        if (value.equals("assistant")) return ASSISTANT;
        if (value.equals("system")) return SYSTEM;
        throw new IOException("Cannot deserialize Role");
    }

}
