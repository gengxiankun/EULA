package com.eula.component.security.aop;

import com.eula.component.security.annotation.Security;
import com.eula.component.security.enums.SecurityType;
import com.eula.component.security.service.DecryptService;
import com.eula.component.security.service.DesensitizedService;
import com.eula.component.security.service.EncryptService;
import com.eula.component.security.service.RsaDecryptService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 信息安全切面，通过注解实现对敏感信息进行加密、脱敏以及对应解密程序
 * @author gengxiankun
 */
@Component
@Aspect
public class SecurityAspect {

    @Value("${plugin.security.key}")
    private String key;

    @Value("${plugin.security.rsa-private-key}")
    private String rasPrivateKey;

    @Resource
    private RsaDecryptService rsaDecryptService;

    @Resource
    private DesensitizedService desensitizedService;

    @Resource
    private EncryptService encryptService;

    @Resource
    private DecryptService decryptService;

    @Around("@annotation(security)")
    public Object around(ProceedingJoinPoint joinPoint, Security security) throws Throwable {
        Object result = null;
        SecurityType[] securityTypes = security.securityTypes();
        // 保证先后顺序，防止解密和脱密乱序而出现异常
        Arrays.sort(securityTypes);
        for (SecurityType securityType : securityTypes) {
            if (securityType.equals(SecurityType.RSA_DECRYPT)) {
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                Object[] args = joinPoint.getArgs();
                this.rsaDecryptService.encrypt(method, args, this.rasPrivateKey);
                if (!Arrays.asList(securityTypes).contains(SecurityType.ENCRYPT)) {
                    result = joinPoint.proceed(args);
                }
            }
            if (securityType.equals(SecurityType.ENCRYPT)) {
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                Object[] args = joinPoint.getArgs();
                this.encryptService.encrypt(method, args, this.key);
                result = joinPoint.proceed(args);
            }
            if (securityType.equals(SecurityType.DECRYPT)) {
                if (result == null) {
                    result = joinPoint.proceed();
                }
                this.decryptService.decrypt(result, this.key);
            }
            if (securityType.equals(SecurityType.DESENSITIZED)) {
                if (result == null) {
                    result = joinPoint.proceed();
                }
                this.desensitizedService.desensitized(result);
            }
        }
        return result;
    }

}
