package com.shiyi.config.security.login;


import com.shiyi.common.ApiResult;
import com.shiyi.common.RedisConstants;
import com.shiyi.entity.User;
import com.shiyi.mapper.UserMapper;
import com.shiyi.utils.*;
import com.shiyi.dto.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  <p> 认证成功处理 </p>
 *
 * @description :
 * @author : by blue
 * @date : 2019/10/12 15:31
 */
@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.expiration}")
    private  Integer expiration;


    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        SecurityUser securityUser = ((SecurityUser) auth.getPrincipal());
        String username = securityUser.getUsername();
        // 将token设置到redis中,时间为1个小时
        Map<String, Object> claims = new HashMap<>();
        claims.put(RedisConstants.LOGIN_PREFIX,username);
        String token = jwtUtils.generateToken(claims);
        redisCache.deleteObject(RedisConstants.LOGIN_PREFIX+securityUser.getUsername());
        redisCache.setCacheObject
                (RedisConstants.LOGIN_PREFIX+securityUser.getUsername(),token,expiration, TimeUnit.MILLISECONDS);
        claims = new HashMap<>();
        claims.put("token",token);

        //新线程去修改登录信息
        new Thread(() -> updateLogin(httpServletRequest,securityUser)).start();
        ResponseUtils.out(response, ApiResult.ok("登录成功!", claims));
    }

    private void updateLogin(HttpServletRequest httpServletRequest,SecurityUser securityUser){
        String ip = IpUtils.getIp(httpServletRequest);
        User vo = new User();
        vo.setIpSource(IpUtils.getCityInfo(ip));
        vo.setIpAddress(ip);
        vo.setLastLoginTime(DateUtils.getNowDate());
        vo.setId(securityUser.getCurrentUserInfo().getId());
        userMapper.updateById(vo);
    }
}
