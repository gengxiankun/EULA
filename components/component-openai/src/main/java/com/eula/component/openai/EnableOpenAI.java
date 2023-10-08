package com.eula.component.openai;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({OpenaiProperties.class, OpenaiAutoConfiguration.class})
public @interface EnableOpenAI {
}
