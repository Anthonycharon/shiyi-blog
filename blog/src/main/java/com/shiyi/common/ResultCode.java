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
    //服务器内部错误
    INTERNAL_SERVER_ERROR( 500, "服务器内部错误" );

    private int code;
    private String desc;

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
