package com.shiyi.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ResponseResult;
import com.shiyi.service.UserAuthService;
import com.shiyi.vo.EmailLoginVO;
import com.shiyi.vo.EmailRegisterVO;
import com.shiyi.vo.QQLoginVO;
import com.shiyi.vo.UserAuthVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@Api(tags = "登录接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiUserController {

    private final UserAuthService userAuthService;

    @RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
    @ApiOperation(value = "邮箱登录", httpMethod = "POST", response = ResponseResult.class, notes = "邮箱登录")
    public ResponseResult emailLogin(@Valid @RequestBody EmailLoginVO emailLoginVO){
        return userAuthService.emailLogin(emailLoginVO);
    }

    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    @ApiOperation(value = "邮箱账号注册", httpMethod = "POST", response = ResponseResult.class, notes = "邮箱账号注册")
    public ResponseResult emailRegister(@Valid @RequestBody EmailRegisterVO emailRegisterVO){
        return userAuthService.emailRegister(emailRegisterVO);
    }

    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    @BusinessLog(value = "个人中心模块-邮箱账号修改密码",type = "修改",desc = "邮箱账号修改密码")
    public ResponseResult updatePassword(@Valid @RequestBody EmailRegisterVO emailRegisterVO){
        return userAuthService.updatePassword(emailRegisterVO);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ApiOperation(value = "QQ登录", httpMethod = "POST", response = ResponseResult.class, notes = "QQ登录")
    public ResponseResult login(@Valid @RequestBody QQLoginVO qqLoginVO){
        return userAuthService.qqLogin(qqLoginVO);
    }

    @RequestMapping(value = "/gitEELogin",method = RequestMethod.GET)
    @ApiOperation(value = "gitEE登录", httpMethod = "GET", response = ResponseResult.class, notes = "gitEE登录")
    public ResponseResult gitEELogin(String code){
        return userAuthService.giteeLogin(code);
    }

    @RequestMapping(value = "/weiboLogin",method = RequestMethod.GET)
    @ApiOperation(value = "微博登录", httpMethod = "GET", response = ResponseResult.class, notes = "微博登录")
    public ResponseResult weiboLogin(String code){
        return userAuthService.weiboLogin(code);
    }

    @RequestMapping(value = "/sendEmailCode",method = RequestMethod.GET)
    @ApiOperation(value = "发送邮箱验证码", httpMethod = "GET", response = ResponseResult.class, notes = "发送邮箱验证码")
    public ResponseResult sendEmailCode(String email){
        return userAuthService.sendEmailCode(email);
    }

    @RequestMapping(value = "/bindEmail",method = RequestMethod.POST)
    @SaCheckLogin
    @BusinessLog(value = "个人中心模块-绑定邮箱",type = "修改",desc = "绑定邮箱")
    public ResponseResult bindEmail(@RequestBody UserAuthVO vo){
        return userAuthService.bindEmail(vo);
    }

    @BusinessLog(value = "个人中心模块-修改用户信息",type = "修改",desc = "修改用户信息")
    @SaCheckLogin
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public ResponseResult updateUser(@RequestBody UserAuthVO vo){
        return userAuthService.updateUser(vo);
    }
}

