package com.shiyi.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.shiyi.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.shiyi.common.ResultCode.*;

/**
 * @author blue
 * @date 2022/3/11
 * @apiNote
 */
@ControllerAdvice(basePackages = "com.shiyi")
@Slf4j
public class GlobalException {
    // 业务异常
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ApiResult BusinessExceptionHandler(BusinessException ex) {
        if (ex.getCode() != -1) {
            log.error("code : " + ex.getCode() + " msg : " + ex.getMessage(), ex);
        }
        if(StringUtils.isBlank(ex.getLocalizedMessage())||StringUtils.isBlank(ex.getMessage())){
            return ApiResult.fail(ERROR.getCode(), ERROR.getDesc());
        }
        return ApiResult.fail(ex.getCode(), ex.getMessage());
    }

    // Assert业务异常
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ApiResult AssertExceptionHandler(IllegalArgumentException ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return ApiResult.fail(ERROR.getCode(),ERROR.getDesc());
        }
        return ApiResult.fail(ex.getMessage());
    }

    // 登录异常
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public ApiResult NotLoginExceptionHandler(NotLoginException ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        return ApiResult.fail(NOT_LOGIN.getCode(),NOT_LOGIN.getDesc());
    }

    // 权限异常
    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public ApiResult NotPermissionExceptionHandler(NotPermissionException ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        return ApiResult.fail(NO_PERMISSION.getCode(),"无此权限：" + ex.getCode());
    }

    // java异常异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult ExceptionHandler(Exception ex) {
        log.error( " msg : " + ex.getMessage(), ex);
        if(StringUtils.isBlank(ex.getLocalizedMessage())){
            return ApiResult.fail(ERROR.getCode(),ERROR.getDesc());
        }
        return ApiResult.fail(ex.getMessage());
    }
}
