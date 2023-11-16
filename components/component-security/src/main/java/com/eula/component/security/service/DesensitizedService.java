package com.eula.component.security.service;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eula.component.security.annotation.DesensitizedField;
import com.eula.component.security.enums.FieldType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 信息脱敏逻辑类
 * @author xiankun.geng
 */
@Service
public class DesensitizedService {

    /**
     * 信息脱敏
     * @param result 出参
     */
    public void desensitized(Object result) throws IllegalAccessException {
        if (result == null) {
            return;
        }
        if (result instanceof String) {
            if (!result.getClass().isAnnotationPresent(DesensitizedField.class)) {
                return;
            }
            result = this.desensitized(
                    result.getClass().getAnnotation(DesensitizedField.class).filedType(), String.valueOf(result));
        } else {
            if (Objects.isNull(result)) {
                return;
            }
            if (result instanceof Page<?>) {
                this.desensitizedList(((Page<?>) result).getContent());
            } else if (result instanceof IPage<?>) {
                this.desensitizedList(((IPage<?>) result).getRecords());
            } else if (result instanceof ArrayList) {
                this.desensitizedList(result);
            } else {
                this.desensitizedObj(result);
            }
            result = result;
        }
    }

    /**
     * 针对数组结构处理信息脱敏
     * @param obj 出参
     */
    private void desensitizedList(Object obj) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(o);
            }
        }
        for (Object object : result) {
            this.desensitizedObj(object);
        }
    }

    /**
     * 针对对象结构处理信息脱敏
     * @param obj 出参
     */
    private void desensitizedObj(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DesensitizedField.class)) {
                field.setAccessible(true);
                if (field.get(obj) == null || StrUtil.isBlank(String.valueOf(field.get(obj)))) {
                    continue;
                }
                String value = this.desensitized(
                        field.getAnnotation(DesensitizedField.class).filedType(), String.valueOf(field.get(obj)));
                field.set(obj, value);
            }
        }
    }

    /**
     * 针对不同类型进行信息脱敏
     * @param fieldType 类型
     * @param value 数据
     * @return 脱敏后的数据
     */
    private String desensitized(FieldType fieldType, String value) {
        switch (fieldType) {
            case PHONE:
                return DesensitizedUtil.mobilePhone(value);
            case EMAIL:
                return DesensitizedUtil.email(value);
            case PASSWORD:
                return DesensitizedUtil.password(value);
            case CARD_NUM:
                return DesensitizedUtil.idCardNum(value, 1, 2);
            case CHINESE_NAME:
                return DesensitizedUtil.chineseName(value);
            case ADDRESS:
                return DesensitizedUtil.address(value, 6);
            case KEY:
                return DesensitizedUtil.idCardNum(value, 3, 0);
            default:
                return value;
        }
    }

}