package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Role;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface RoleService extends IService<Role> {


    ResponseResult listData(String name);

     ResponseResult addRole(Role role);

    ResponseResult updateRole(Role role);

    ResponseResult delete(List<Integer> ids);

    ResponseResult queryByUser();

    ResponseResult queryRoleId(Integer roleId);

}
