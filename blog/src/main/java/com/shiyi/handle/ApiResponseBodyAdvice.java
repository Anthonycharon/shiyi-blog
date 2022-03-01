package com.shiyi.handle;

import com.shiyi.common.ApiResult;
import com.shiyi.exception.BusinessException;
import com.shiyi.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author blue
 * @description: 统一异常处理类
 * @date 2021/7/30 16:52
 */
@ControllerAdvice(basePackages = "com.shiyi")
@Slf4j
public class ApiResponseBodyAdvice {

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResult BusinessExceptionHandler(BusinessException ex) {
        if (ex.getCode() != -1) {
            log.error("code : " + ex.getCode() + " msg : " + ex.getMessage(), ex);
        }
        if(StringUtils.isBlank(ex.getLocalizedMessage())||StringUtils.isBlank(ex.getMessage())){
            return ApiResult.fail(ErrorCode.ERROR.getCode(),ErrorCode.ERROR.getMsg());
        }
        return ApiResult.fail(ex.getCode(), ex.getMessage());
    }

    // Assert业务异常
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResult AssertExceptionHandler(IllegalArgumentException ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return ApiResult.fail(ErrorCode.ERROR.getCode(),ErrorCode.ERROR.getMsg());
        }
        return ApiResult.fail(ex.getMessage());
    }

    // java异常异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult ExceptionHandler(Exception ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return ApiResult.fail(ErrorCode.ERROR.getCode(),ErrorCode.ERROR.getMsg());
        }
        return ApiResult.fail(ex.getMessage());
    }

}
