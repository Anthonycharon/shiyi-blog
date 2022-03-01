package com.shiyi.controller.system;

import com.shiyi.annotation.IgnoreUrl;
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
    @ApiOperation(value = "用户列表", httpMethod = "GET", response = ApiResult.class, notes = "用户列表")
    public ApiResult listPage(String username,Integer loginType,Integer pageNo, Integer pageSize) {
        return userService.listData(username,loginType,pageNo,pageSize);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "添加用户", httpMethod = "POST", response = ApiResult.class, notes = "添加用户")
    @OperationLogger(value = "添加用户")
    public ApiResult create(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "用户详情", httpMethod = "GET", response = ApiResult.class, notes = "用户详情")
    public ApiResult info(Integer id) {
        return userService.info(id);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改用户", httpMethod = "POST", response = ApiResult.class, notes = "修改用户")
    @OperationLogger(value = "修改用户")
    public ApiResult update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除用户", httpMethod = "DELETE", response = ApiResult.class, notes = "删除用户")
    @OperationLogger(value = "删除用户")
    public ApiResult remove(@RequestBody List<Integer> ids) {
        return userService.delete(ids);
    }

    @PostMapping(value = "/getCurrentUserInfo")
    @ApiOperation(value = "获取当前登录用户信息", httpMethod = "POST", response = ApiResult.class, notes = "获取当前登录用户信息")
    @IgnoreUrl
    public ApiResult getCurrentUserInfo(HttpServletRequest request) {
        String token = request.getHeader(Constants.REQUEST_HEADER);
        SystemUserDTO userInfo = userService.getCurrentUserInfo(token);
        return ApiResult.ok(200, "获取当前登录用户信息成功", userInfo);
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "退出登录", httpMethod = "POST", response = ApiResult.class, notes = "退出登录")
    public ApiResult logout(HttpServletRequest request) {
        return userService.logout(request.getHeader(Constants.REQUEST_HEADER));
    }

    @PostMapping(value = "/getUserMenu")
    @ApiOperation(value = "获取用户菜单", httpMethod = "POST", response = ApiResult.class, notes = "获取用户菜单")
    public ApiResult getUserMenu(HttpServletRequest request) {
        return userService.getUserMenu(request.getHeader(Constants.REQUEST_HEADER));
    }

    @PostMapping(value = "/updatePassword")
    @ApiOperation(value = "修改密码", httpMethod = "POST", response = ApiResult.class, notes = "修改密码")
    @OperationLogger(value = "修改密码")
    public ApiResult updatePassword(HttpServletRequest request,@RequestBody Map<String,String> map) {
        return userService.updatePassword(request.getHeader(Constants.REQUEST_HEADER),map);
    }
    @GetMapping(value = "/online")
    @ApiOperation(value = "查看在线用户", httpMethod = "GET", response = ApiResult.class, notes = "查看在线用户")
    @IgnoreUrl
    public ApiResult listOnlineUsers(String keywords,int pageNo,int pageSize) {
        return userAuthService.listOnlineUsers(keywords,pageNo,pageSize);
    }
}
