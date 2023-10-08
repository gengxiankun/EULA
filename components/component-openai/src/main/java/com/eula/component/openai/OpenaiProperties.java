package com.eula.component.openai;

import com.eula.component.openai.enums.Model;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "component.openai")
public class OpenaiProperties {

    private String url;

    private Model model;

    private String key;

    private Integer readTimeout = 100;

    private Integer connectTimeout = 60;

    private Integer writeTimeout = 60;

}
