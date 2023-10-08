package com.eula.component.result;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.ServletRequest;

/**
 * 业务接口回参处理拦截器
 * @author xiankun.geng
 */
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(ResponseResultHandler.class);

    /**
     * 是否处理回参的标识
     */
    public final static String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ServletRequest request = sra.getRequest();
        ResponseResult responseResult = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResult == null ? false : true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Result result = Result.success(body);
        // 兼容字符串返回类型
        if (body instanceof String) {
             return JSON.toJSONString(result);
        }
        return result;
    }

    /**
     * 全局异常处理
     *
     * @param err 异常类
     *
     * @return Result
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(Exception err) {
        // 异常情况下移除 RESPONSE_RESULT_ANN 注解，数据直接在此处返回
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ServletRequest request = sra.getRequest();
        request.removeAttribute(RESPONSE_RESULT_ANN);
        // 处理业务异常类
        if (err instanceof ResultException) {
            ResultException resultException = (ResultException) err;
            return Result.failure(resultException.getCode(), resultException.getMessage());
        }
        // 处理 @Validated 参数校验失败异常
        if (err instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException= (MethodArgumentNotValidException) err;
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            String defaultMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            return Result.failure(ResultCode.SYSTEM_EXCEPTION.getCode(), defaultMessage);
        }
        // 处理异常缘由也是业务引起的错误信息
        if (err.getCause() instanceof ResultException) {
            ResultException resultException = (ResultException) err.getCause();
            return Result.failure(resultException.getCode(), resultException.getMessage());
        }
        // Request Valid 异常处理
        if (err instanceof BindException) {
            String defaultMessage = ((BindException) err).getAllErrors().get(0).getDefaultMessage();
            return Result.failure(ResultCode.SYSTEM_EXCEPTION.getCode(), defaultMessage);
        }
        if (err instanceof BeanInstantiationException) {
            Throwable cause = ((BeanInstantiationException) err).getCause();
            return Result.failure(ResultCode.SYSTEM_EXCEPTION.getCode(), cause.getMessage());
        }
        err.printStackTrace();
        // 其他异常统一返回 UNKNOWN_EXCEPTION 错误码
        // TODO: 需根据环境区别报错信息
        return Result.failure(ResultCode.SYSTEM_EXCEPTION.getCode(), err.getMessage());
    }

}
