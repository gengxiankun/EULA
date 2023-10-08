package com.eula.component.security.annotation;

import com.eula.component.security.enums.SecurityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gengxiankun
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {

    SecurityType[] securityTypes();

}
