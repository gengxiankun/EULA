package com.eula.component.security.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.eula.component.security.annotation.EncryptField;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 敏感信息加密逻辑类
 * @author gengxiankun
 */
@Service
public class EncryptService {

    /**
     * 敏感信息加密
     * @param args 入参
     */
    public void encrypt(Method method, Object[] args, String key) throws IllegalAccessException {
        if (args.length == 0) {
            return;
        }
        SymmetricCrypto aes = new SymmetricCrypto(
                SymmetricAlgorithm.AES,
                SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded());
        // 参数注解，一维是参数，二维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                if (args[i] == null) {
                    continue;
                }
                Annotation[] annotation = annotations[i];
                for (Annotation pa : annotation) {
                    if (pa.annotationType().equals(EncryptField.class)) {
                        args[i] = aes.encryptHex(String.valueOf(args[i]));
                    }
                }
            } else {
                if (Objects.isNull(args[i])) {
                    continue;
                }
                Field[] fields = args[i].getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(EncryptField.class)) {
                        field.setAccessible(true);
                        if (field.get(args[i]) == null || CharSequenceUtil.isBlank(String.valueOf(field.get(args[i])))) {
                            continue;
                        }
                        aes.encryptHex(String.valueOf(field.get(args[i])));
                        field.set(args[i], aes.encryptHex(String.valueOf(field.get(args[i]))));
                    }
                }
            }
        }
    }

}