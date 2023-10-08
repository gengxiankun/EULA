package com.eula.component.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.eula.component.security.annotation.RsaDecryptField;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Rsa 非对称解密逻辑类
 * @author xiankun.geng
 */
@Service
public class RsaDecryptService {

    public void encrypt(Method method, Object[] args, String key) throws IllegalAccessException {
        if (args.length == 0) {
            return;
        }
        // 初始化私钥工具
        RSA rsa = new RSA(key, null);
        // 参数注解，一维是参数，二维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                if (args[i] == null) {
                    continue;
                }
                Annotation[] annotation = annotations[i];
                for (Annotation pa : annotation) {
                    if (pa.annotationType().equals(RsaDecryptField.class)) {
                        args[i] = rsa.decryptStr(String.valueOf(args[i]), KeyType.PrivateKey);
                    }
                }
            } else {
                if (Objects.isNull(args[i])) {
                    continue;
                }
                Field[] fields = args[i].getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(RsaDecryptField.class)) {
                        field.setAccessible(true);
                        if (field.get(args[i]) == null || CharSequenceUtil.isBlank(String.valueOf(field.get(args[i])))) {
                            continue;
                        }
                        rsa.decryptStr(String.valueOf(field.get(args[i])), KeyType.PrivateKey);
                        field.set(args[i], rsa.decryptStr(String.valueOf(field.get(args[i])), KeyType.PrivateKey));
                    }
                }
            }
        }
    }
}