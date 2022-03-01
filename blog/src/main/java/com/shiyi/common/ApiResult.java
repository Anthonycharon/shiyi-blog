package com.shiyi.common;


import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 *  <p> API返回参数 </p>
 *
 * @description :
 * @author : by blue
 * @date : 2019/7/20 11:09
 */
@ApiModel(value = "API返回参数")
public class ApiResult {
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "响应消息", required = false)
    private String message;

    /**
     * 响应码：参考`ResultCode`
     */
    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    /**
     * 响应中的数据
     */
    @ApiModelProperty(value = "响应数据", required = false)
    private Object data;

    private Map<String,Object> extra = new HashMap<>();

    public ApiResult putExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }

    /***
     * 过期
     *
     * @param message:
     * @return: ApiResult
     */
    public static ApiResult expired(String message) {
        return new ApiResult(ResultCode.UN_LOGIN.getCode(), message, null);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(ResultCode.FAILURE.getCode(), message, null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code
     * @param message:
     * @return: ApiResult
     */
    public static ApiResult fail(Integer code, String message) {
        return new ApiResult(code, message, null);
    }

    public static ApiResult ok(String message) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, null);
    }

    public static ApiResult ok() {
        return new ApiResult(ResultCode.SUCCESS.getCode(), "OK", null);
    }

    public static ApiResult build(Integer code, String msg, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), msg, data);
    }

    public static ApiResult ok(String message, Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(), message, data);
    }
    public static ApiResult success(Object data) {
        return new ApiResult(ResultCode.SUCCESS.getCode(),"操作成功", data);
    }
    /**
     * 自定义返回码
     */
    public static ApiResult ok(Integer code, String message) {
        return new ApiResult(code, message);
    }

    /**
     * 自定义
     *
     * @param code：验证码
     * @param message：返回消息内容
     * @param data：返回数据
     * @return: ApiResult
     */
    public static ApiResult ok(Integer code, String message, Object data) {
        return new ApiResult(code, message, data);
    }

    public ApiResult() { }

    public static ApiResult build(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public ApiResult(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ApiResult(Object data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = "OK";
        this.data = data;
    }

    public ApiResult(String message) {
        this(ResultCode.SUCCESS.getCode(), message, null);
    }

    public ApiResult(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
