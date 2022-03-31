package com.shiyi.common;


/**
 *  <p> 响应码枚举 - 可参考HTTP状态码的语义 </p>
 *
 * @description :
 * @author : by blue
 * @date : 2019/8/22 11:09
 */
public enum ResultCode {
    //成功
    SUCCESS( 200, "SUCCESS" ),
    //失败
    FAILURE( 400, "FAILURE" ),
    /**
     * qq登录错误
     */
    QQ_LOGIN_ERROR(53001, "qq登录错误"),
    /**
     * 微博登录错误
     */
    WEIBO_LOGIN_ERROR(53002, "微博登录错误"),

    GITEE_LOGIN_ERROR(53002, "gitee登录错误"),
    //服务器内部错误
    INTERNAL_SERVER_ERROR( 500, "服务器内部错误" ),

    // 系统级别错误码
    ERROR(-1, "操作异常"),
    ERROR_DEFAULT(500,"系统繁忙，请稍后重试"),
    NOT_LOGIN(401, "请先登录!"),
    ILLegal(-6, "非法操作!"),
    NO_PERMISSION(-7,"无权限"),
    ERROR_PASSWORD(-8,"用户帐号或者密码错误!"),
    DISABLE_ACCOUNT(-9,"帐号已被禁用!"),
    EMAIL_ERROR(-10,"邮箱格式不对，请检查后重试!"),

    // 服务层面
    ERROR_EXCEPTION_MOBILE_CODE(10003,"验证码不正确或已过期，请重新输入"),
    ERROR_USER_NOT_EXIST(10009, "用户不存在"),
    ERROR_MUST_REGISTER(10017,"请先注册帐号!"),
    EXIST_USER(10018,"用户名已存在,请重试!");

    public int code;
    public String desc;

    ResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
