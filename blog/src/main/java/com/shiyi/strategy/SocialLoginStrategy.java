package com.shiyi.strategy;

import com.shiyi.dto.UserInfoDTO;

/**
 * @author blue
 * @date 2022/1/5
 * @apiNote
 */
public interface SocialLoginStrategy {
    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoDTO} 用户信息
     */
    UserInfoDTO login(String data);
}
