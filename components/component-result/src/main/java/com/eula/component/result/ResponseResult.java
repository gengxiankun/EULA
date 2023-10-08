package com.eula.component.result;

import java.lang.annotation.*;

/**
 * 业务接口回参处理注解类
 * @author xiankun.geng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {
}
