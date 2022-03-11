package com.shiyi.controller.system;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.shiyi.config.satoken.MySaTokenListener;
import com.shiyi.config.satoken.OnlineUser;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.common.Constants;
import com.shiyi.entity.User;
import com.shiyi.service.UserAuthService;
import com.shiyi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:12
 */
@RestController
@RequestMapping("/system/user")
@Api(tags = "系统用户管理-接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAuthService userAuthService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "用户列表", httpMethod = "GET", response = ApiResult.class, notes = "用户列表")
    public ApiResult listPage(String username,Integer loginType,Integer pageNo, Integer pageSize) {
        return userService.listData(username,loginType,pageNo,pageSize);
    }

    @PostMapping(value = "/create")
    @SaCheckPermission("/system/user/create")
    @ApiOperation(value = "添加用户", httpMethod = "POST", response = ApiResult.class, notes = "添加用户")
    @OperationLogger(value = "添加用户")
    public ApiResult create(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/user/info")
    @ApiOperation(value = "用户详情", httpMethod = "GET", response = ApiResult.class, notes = "用户详情")
    public ApiResult info(Integer id) {
        return userService.info(id);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/user/update")
    @ApiOperation(value = "修改用户", httpMethod = "POST", response = ApiResult.class, notes = "修改用户")
    @OperationLogger(value = "修改用户")
    public ApiResult update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/user/remove")
    @ApiOperation(value = "删除用户", httpMethod = "DELETE", response = ApiResult.class, notes = "删除用户")
    @OperationLogger(value = "删除用户")
    public ApiResult remove(@RequestBody List<Integer> ids) {
        return userService.delete(ids);
    }

    @PostMapping(value = "/getCurrentUserInfo")
    @SaCheckLogin
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "POST", response = ApiResult.class, notes = "获取当前登录用户信息")
    public ApiResult getCurrentUserInfo() {
        return ApiResult.ok(200, "获取当前登录用户信息成功", userService.getCurrentUserInfo());
    }

    @PostMapping(value = "/getUserMenu")
    @SaCheckLogin
    @ApiOperation(value = "获取用户菜单", httpMethod = "POST", response = ApiResult.class, notes = "获取用户菜单")
    public ApiResult getUserMenu() {
        return userService.getUserMenu();
    }

    @PostMapping(value = "/updatePassword")
    @SaCheckPermission("/system/user/updatePassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST", response = ApiResult.class, notes = "修改密码")
    @OperationLogger(value = "修改密码")
    public ApiResult updatePassword(@RequestBody Map<String,String> map) {
        return userService.updatePassword(map);
    }

    @GetMapping(value = "/online")
    @SaCheckLogin
    @ApiOperation(value = "查看在线用户", httpMethod = "GET", response = ApiResult.class, notes = "查看在线用户")
    public ApiResult listOnlineUsers(String keywords,int pageNo,int pageSize) {
        return userService.listOnlineUsers(keywords,pageNo,pageSize);
    }
}
