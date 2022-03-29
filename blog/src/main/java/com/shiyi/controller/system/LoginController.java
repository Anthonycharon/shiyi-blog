package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.common.ApiResult;
import com.shiyi.common.RedisConstants;
import com.shiyi.service.LoginService;
import com.shiyi.utils.RandomUtil;
import com.shiyi.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:37
 */
@RestController
@Api(tags = "登录-接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final LoginService loginService;

    @RequestMapping(value = "/captchaImage",method = RequestMethod.GET)
    @ApiOperation(value = "获取验证码", httpMethod = "GET", response = ApiResult.class, notes = "获取验证码")
    public ApiResult getCode(HttpServletResponse response) throws IOException {
        Map<String,String> result = loginService.getCode(response);
        return ApiResult.ok("获取验证码成功",result);
    }


    @PostMapping("login")
    public ApiResult doLogin(@Validated @RequestBody LoginVO vo) {
        return loginService.doLogin(vo);
    }

    @RequestMapping("isLogin")
    public ApiResult isLogin() {
        return ApiResult.success("当前会话是否登录：" + StpUtil.isLogin());
    }

    @SaCheckLogin
    @GetMapping("logout")
    public ApiResult logout() {
        StpUtil.logout();
        return ApiResult.ok("退出成功");
    }
}
