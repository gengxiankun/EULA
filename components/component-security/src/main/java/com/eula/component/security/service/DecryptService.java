package com.eula.component.security.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eula.component.security.annotation.EncryptField;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 加密信息解密逻辑类
 * @author xiankun.geng
 */
@Service
public class DecryptService {

    /**
     * 加密信息解密
     * @param result 出参
     */
    public void decrypt(Object result, String key) throws IllegalAccessException {
        if (result != null) {
            SymmetricCrypto aes = new SymmetricCrypto(
                    SymmetricAlgorithm.AES,
                    SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), key.getBytes()).getEncoded());
            if (result instanceof String) {
                result = aes.decryptStr(String.valueOf(result));
            } else {
                if (Objects.isNull(result)) {
                    return;
                }
                if (result instanceof Page<?>) {
                    decryptList(aes, ((Page<?>) result).getContent());
                } else if (result instanceof IPage<?>) {
                    decryptList(aes, ((IPage<?>) result).getRecords());
                } else if (result instanceof ArrayList) {
                    decryptList(aes, result);
                } else {
                    decryptObj(aes, result);
                }
            }
        }
    }

    private void decryptList(SymmetricCrypto aes, Object obj) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(o);
            }
        }
        for (Object object : result) {
            decryptObj(aes, object);
        }
    }

    private void decryptObj(SymmetricCrypto aes, Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptField.class)) {
                field.setAccessible(true);
                if (field.get(obj) == null || StrUtil.isBlank(String.valueOf(field.get(obj)))) {
                    continue;
                }
                String value = null;
                try {
                    value = aes.decryptStr((String) field.get(obj));
                } catch (CryptoException exception) {
                    value = (String) field.get(obj);
                    exception.printStackTrace();
                }
                field.set(obj, value);
            }
        }
    }

}
