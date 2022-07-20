package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.dto.SystemUserVO;
import com.shiyi.entity.User;

import java.util.List;
import java.util.Map;

/**
 *
 * @author blue
 * @date: 2021/7/30 17:17
 */
public interface UserService extends IService<User> {

    ResponseResult listData(String username, Integer loginType);

    ResponseResult info(Integer id);

    ResponseResult saveUser(User user);

    ResponseResult updateUser(User user);

    ResponseResult delete(List<Integer> ids);

    SystemUserVO getCurrentUserInfo();

    ResponseResult getUserMenu();

    ResponseResult updatePassword(Map<String, String> map);

    ResponseResult listOnlineUsers(String keywords);

    ResponseResult kick(String token);
}
