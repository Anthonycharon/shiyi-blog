package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @author blue
 * @date: 2021/7/30 17:17
 */
public interface UserService extends IService<User> {

    ApiResult listData(String username,Integer loginType, Integer pageNo, Integer pageSize);

    ApiResult info(Integer id);

    ApiResult saveUser(User user);

    ApiResult updateUser(User user);

    ApiResult delete(List<Integer> ids);

    ApiResult logout(String token);

    SystemUserDTO getCurrentUserInfo(String token);

    ApiResult getUserMenu(String header);

    ApiResult updatePassword(String header, Map<String, String> map);

}
