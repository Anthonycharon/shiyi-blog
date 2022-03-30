package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Role;
import com.shiyi.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理-接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final RoleService roleService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "角色列表", httpMethod = "GET", response = ApiResult.class, notes = "角色列表")
    public ApiResult listRole(String name) {
        return roleService.listData(name);
    }

    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "获取当前登录用户所拥有的权限", httpMethod = "GET", response = ApiResult.class, notes = "获取当前登录用户所拥有的权限")
    public ApiResult queryByUser() {
        return roleService.queryByUser();
    }

    @RequestMapping(value = "queryRoleId", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "获取该角色所有的权限", httpMethod = "GET", response = ApiResult.class, notes = "获取该角色所有的权限")
    public ApiResult queryRoleId(Integer roleId) {
        return roleService.queryRoleId(roleId);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @SaCheckPermission("/system/role/create")
    @ApiOperation(value = "添加角色", httpMethod = "POST", response = ApiResult.class, notes = "添加角色")
    @OperationLogger(value = "添加角色")
    public ApiResult create(@RequestBody Role role) {
        return roleService.addRole(role);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @SaCheckPermission("/system/role/update")
    @ApiOperation(value = "修改角色", httpMethod = "POST", response = ApiResult.class, notes = "修改角色")
    @OperationLogger(value = "修改角色")
    public ApiResult update(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "remove", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/role/remove")
    @ApiOperation(value = "删除角色", httpMethod = "DELETE", response = ApiResult.class, notes = "删除角色")
    @OperationLogger(value = "删除角色")
    public ApiResult remove(@RequestBody List<Integer> ids) {
        return roleService.delete(ids);
    }


}
