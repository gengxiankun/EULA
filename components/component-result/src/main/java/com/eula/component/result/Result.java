package com.eula.component.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 业务接口统一回参格式
 * @author xiankun.geng
 */
@Setter
@Getter
public class Result<T> implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 返回数据体
     */
    private T data;

    public Result() {
    }

    public Result(ResultCodeInterface resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCodeInterface resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, processingData(data));
    }

    public static Result failure(String code, String message) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result failure(String code, String message, String cause) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public boolean check() {
        return this.code.equals(ResultCode.SUCCESS.getCode());
    }

    /**
     * 处理返回数据，针对 list 结构和 Page 结构进行格式化
     *
     * @param data 返回数据
     *
     * @return Object
     */
    private static Object processingData(Object data) {
        Object processingData;
        if (data instanceof Page<?>) {
            Page<?> page = (Page<?>) data;
            processingData = new ResultListVo<>(page.getContent(), new ResultPagination(page));
        } else if (data instanceof List<?>) {
            processingData = new ResultListVo<>(data);
        } else if (data instanceof IPage<?>) {
            IPage<?> page = (IPage<?>) data;
            processingData = new ResultListVo<>(page.getRecords(), new ResultPagination(page));
        } else {
            processingData = data;
        }
        return processingData;
    }

}
