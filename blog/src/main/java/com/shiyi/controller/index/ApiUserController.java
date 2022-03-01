package com.shiyi.controller.index;

import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.common.Constants;
import com.shiyi.common.RedisConstants;
import com.shiyi.service.UserAuthService;
import com.shiyi.utils.JwtUtils;
import com.shiyi.utils.RedisCache;
import com.shiyi.vo.EmailLoginVO;
import com.shiyi.vo.EmailRegisterVO;
import com.shiyi.vo.QQLoginVO;
import com.shiyi.vo.UserAuthVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/user")
@RestController
@Api(tags = "登录接口")
public class ApiUserController {

    @Autowired
    UserAuthService userAuthService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    JwtUtils jwtUtils;

    //邮箱登录
    @RequestMapping(value = "/emailLogin",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult emailLogin(@Valid @RequestBody EmailLoginVO EmailLoginVO){
        return userAuthService.emailLogin(EmailLoginVO);
    }

    //邮箱账号注册
    @RequestMapping(value = "/emailRegister",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult emailRegister(@Valid @RequestBody EmailRegisterVO emailRegisterVO){
        return userAuthService.emailRegister(emailRegisterVO);
    }

    //邮箱账号忘记密码
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult updatePassword(@Valid @RequestBody EmailRegisterVO emailRegisterVO){
        return userAuthService.updatePassword(emailRegisterVO);
    }

    //QQ登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult login(@Valid @RequestBody QQLoginVO qqLoginVO){
        return userAuthService.qqLogin(qqLoginVO);
    }

    //gitEE登录
    @RequestMapping(value = "/gitEELogin",method = RequestMethod.GET)
    @IgnoreUrl
    public ApiResult gitEELogin(String code){
        return userAuthService.giteeLogin(code);
    }

    //微博登录
    @RequestMapping(value = "/weiboLogin",method = RequestMethod.GET)
    @IgnoreUrl
    public ApiResult weiboLogin(String code){
        return userAuthService.weiboLogin(code);
    }

    @BusinessLog(value = "登录模块-用户退出登录",type = "查询",desc = "退出登录")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult logout(){
         return userAuthService.logout();
    }

    @RequestMapping(value = "/sendEmailCode",method = RequestMethod.GET)
    @IgnoreUrl
    public ApiResult sendEmailCode(String email){
        return userAuthService.sendEmailCode(email);
    }

    @RequestMapping(value = "/bindEmail",method = RequestMethod.POST)
    @BusinessLog(value = "个人中心模块-绑定邮箱",type = "修改",desc = "绑定邮箱")
    @IgnoreUrl
    public ApiResult bindEmail(@RequestBody UserAuthVO vo){
        return userAuthService.bindEmail(vo);
    }

    @BusinessLog(value = "个人中心模块-修改用户信息",type = "修改",desc = "修改用户信息")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    @IgnoreUrl
    public ApiResult updateUser(@RequestBody UserAuthVO vo){
        return userAuthService.updateUser(vo);
    }
}

