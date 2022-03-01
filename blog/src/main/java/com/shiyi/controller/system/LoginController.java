package com.shiyi.controller.system;

import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.dto.LoginUserDTO;
import com.shiyi.common.ApiResult;
import com.shiyi.common.RedisConstants;
import com.shiyi.service.LoginService;
import com.shiyi.utils.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/generateCode")
    @ApiOperation(value = "生成用户邀请码", httpMethod = "GET", response = ApiResult.class, notes = "生成用户邀请码")
    public ApiResult generateCode() {
        String code = RandomUtil.generationCapital(10);
        redisTemplate.opsForValue().set(RedisConstants.INVITATION_CODE_PREFIX,code,24, TimeUnit.HOURS);
        return ApiResult.ok("生成邀请码成功!", code);
    }

    @RequestMapping(value = "/captchaImage",method = RequestMethod.GET)
    @IgnoreUrl
    @ApiOperation(value = "获取验证码", httpMethod = "GET", response = ApiResult.class, notes = "获取验证码")
    public ApiResult getCode(HttpServletResponse response) throws IOException {
        Map<String,String> result = loginService.getCode(response);
        return ApiResult.ok("获取验证码成功",result);
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", httpMethod = "POST", response = ApiResult.class, notes = "登录")
    public void login(HttpServletResponse response,@RequestBody LoginUserDTO loginUserDTO) {

    }
}
