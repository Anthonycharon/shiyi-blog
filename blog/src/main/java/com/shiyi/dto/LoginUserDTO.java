package com.shiyi.dto;

import lombok.Data;

/**
 * @author blue
 * @description: 登录用户入参
 * @date 2021/7/30 14:40
 */
@Data
public class LoginUserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码唯一标识
     */
    private String uuid;


}
