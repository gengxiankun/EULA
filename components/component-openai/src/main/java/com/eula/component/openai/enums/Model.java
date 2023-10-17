package com.eula.component.openai.enums;

import java.io.IOException;

public enum Model {

    GPT_35_TURBO, GPT_35_TURBO_0301, GPT_35_TURBO_0613, GPT_35_TURBO_16_K, GPT_35_TURBO_16_K_0613, GPT_4, GPT_40613;

    public String toValue() {
        switch (this) {
            case GPT_35_TURBO:
                return "gpt-3.5-turbo";
            case GPT_35_TURBO_0301:
                return "gpt-3.5-turbo-0301";
            case GPT_35_TURBO_0613:
                return "gpt-3.5-turbo-0613";
            case GPT_35_TURBO_16_K:
                return "gpt-3.5-turbo-16k";
            case GPT_35_TURBO_16_K_0613:
                return "gpt-3.5-turbo-16k-0613";
            case GPT_4:
                return "gpt-4";
            case GPT_40613:
                return "gpt-4-0613";
        }
        return null;
    }

    public static Model forValue(String value) throws IOException {
        if (value.equals("gpt-3.5-turbo")) return GPT_35_TURBO;
        if (value.equals("gpt-3.5-turbo-0301")) return GPT_35_TURBO_0301;
        if (value.equals("gpt-3.5-turbo-0613")) return GPT_35_TURBO_0613;
        if (value.equals("gpt-3.5-turbo-16k")) return GPT_35_TURBO_16_K;
        if (value.equals("gpt-3.5-turbo-16k-0613")) return GPT_35_TURBO_16_K_0613;
        if (value.equals("gpt-4")) return GPT_4;
        if (value.equals("gpt-4-0613")) return GPT_40613;
        throw new IOException("Cannot deserialize Model");
    }

}
