package com.eula.component.result;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 业务接口回参预处理拦截器
 * @author xiankun.geng
 */
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

    /**
     * 是否处理回参的标识
     */
    public static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    /**
     * 在业务处理器处理之前被调用，用于获取当前业务是否注解了 ResponseResult
     *
     * @param request  客户端请求
     * @param response 服务器响应
     * @param handler  控制器方法
     *
     * @return Boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();
            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN, clazz.getAnnotation(ResponseResult.class));
            } else if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(RESPONSE_RESULT_ANN, method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
