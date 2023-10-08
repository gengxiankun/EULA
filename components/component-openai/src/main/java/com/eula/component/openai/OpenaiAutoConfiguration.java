package com.eula.component.openai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class OpenaiAutoConfiguration {

    @Resource
    private OpenaiProperties properties;

    @Bean
    public OpenaiTemplate openaiTemplate() {
        OpenaiTemplate template = new OpenaiTemplate();
        template.setProperties(properties);
        return template;
    }

}
