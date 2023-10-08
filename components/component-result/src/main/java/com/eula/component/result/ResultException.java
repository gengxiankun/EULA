package com.eula.component.result;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务接口异常类
 *
 * @author xiankun.geng
 */
@Setter
@Getter
public class ResultException extends RuntimeException {

    /**
     * 业务接口异常错误码
     */
    private String code;

    public ResultException() {
        super();
    }

    public ResultException(ResultCodeInterface resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public ResultException(ResultCodeInterface resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
    }

    public ResultException(String code, String message) {
        super(message);
        this.code = code;
    }

}
