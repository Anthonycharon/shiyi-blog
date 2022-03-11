package com.shiyi.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author blue
 * @date 2022/3/11
 * @apiNote
 */
@Data
public class LoginVO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
