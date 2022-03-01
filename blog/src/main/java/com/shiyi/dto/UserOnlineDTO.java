package com.shiyi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 在线用户
 *
 * @author yezhiqiu
 * @date 2021/08/01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlineDTO {

    /**
     * 用户信息id
     */
    private Integer id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户登录ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

}
