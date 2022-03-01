package com.shiyi.exception;

/**
 * @author blue
 * @description: 异常code
 * @date 2021/7/19 10:41
 */
public enum ErrorCode {
    // 系统级别错误码
    SUCCESS(0,"操作成功"),
    ERROR(-1, "操作异常"),
    FAILURE( 400, "FAILURE" ),
    ERROR_DEFAULT(500,"系统繁忙，请稍后重试"),
    ERROR_INVALID_PARAMS(-2,"请求参数缺失或无效:"),
    ERROR_HTTP_REQUEST_FAIL(-3,"HTTP调用失败"),
    ERROR_EXCEPTION_DATA(-4, "数据异常"),
    NOT_LOGIN(-5, "请先登录!"),
    ILLegal(-6, "非法操作!"),
    UNAUTHORIZED(-7,"未授权"),
    ERROR_PASSWORD(-8,"用户帐号或者密码错误!"),
    DISABLE_ACCOUNT(-9,"帐号已被禁用!"),
    EMAIL_ERROT(-10,"邮箱格式不对，请检查后重试!"),
    MOBILE_ERROT(-10,"电话号码格式不对，请检查后重试!"),

    // DB 层面异常
    ERROR_EXCEPTION_DB_DML(-10,"DB操作异常"),
    ERROR_INVALID_DB_RECORD(-11,"数据库无此记录"),
    ERROR_EXIST_DB_RECORD(-12, "请勿重复提交"),
    ERROR_UPDATE_FAIL(-13, "更新失败"),
    EXIST_ACCOUNT(-14,"该手机号码已注册"),
    REGIST_ERROR(-15,"该手机号码已注册"),

    // Redis 层面异常
    ERROR_EXCEPTION_REDIS_ILLEGAL_IP_PORT(-20,"Redis连接，ip或port不合法"),
    ERROR_EXCEPTION_REDIS_CONFIGURATION_FILE(-21,"解析jedis配置文件失败"),
    ERROR_EXCEPTION_REDIS_CONNECTION(-22,"Redis连接失败"),
    ERROR_EXCEPTION_REDIS_DML(-23,"Redis操作异常"),

    //Token层面的错误
    EXPIRE_TOKEN(-31,"Token已失效,请重新获取!"),

    //校验层面
    ILLEGAL_MOBILE_PATTERN(-41,"不是手机号码格式"),

    // 服务层面
    ERROR_EXCEPTION_USERS(10001,"账号不存在或密码错误"),
    ERROR_REPEAT_PHONENUMBER(10002,"手机号已被注册"),
    ERROR_EXCEPTION_MOBILE_CODE(10003,"验证码不正确或已过期，请重新输入"),
    ERROR_EXCEPTION_NETWORK(10004,"网络异常"),
    ERROR_PASSWORD_NULL(10008, "没有设置密码，请验证码登陆后设置密码"),
    ERROR_USER_NORECORD(10009, "用户不存在"),
    ERROR_MUST_REGIST(10017,"请先注册帐号!"),
    EXIST_USER(10018,"用户名已存在,请重试!"),
    INVITATION_CODE_ERROR(10019,"邀请码不存在,请找管理员获得邀请码!"),

    //电梯控制请求
    EXPIRE_VERIFY_CODE(20002,"验证码已过期,请刷新验证码"),
    FAIL_VERIFY_CODE(20003,"验证码错误,请重新验证"),
    ILLEGAL_REPEAT_REQUEST_IMAGE(20006,"1分钟之内最多只可以请求5次");

    private int code ;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 枚举类型的判断和获取
     * @param code 错误码
     * @return 返回错误码对应的枚举信息
     */
    public static ErrorCode statusOf(int code){
        for(ErrorCode error : values()){
            if(error.getCode() == code){
                return error;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? "" : msg.trim();
    }
}
