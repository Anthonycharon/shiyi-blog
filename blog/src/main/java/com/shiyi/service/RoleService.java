package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Role;

import javax.servlet.http.HttpServletRequest;
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


    ApiResult listData(String name, Integer pageNo, Integer pageSize);

     ApiResult addRole(Role role);

    ApiResult updateRole(Role role);

    ApiResult delete(List<Integer> ids);

    ApiResult queryByUser();

    ApiResult queryRoleId(Integer roleId);

}
