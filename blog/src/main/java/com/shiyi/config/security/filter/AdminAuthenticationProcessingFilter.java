package com.shiyi.config.security.filter;

import com.alibaba.fastjson.JSONObject;
import com.shiyi.dto.LoginUserDTO;
import com.shiyi.common.Constants;
import com.shiyi.common.RedisConstants;
import com.shiyi.utils.MultiReadHttpServletRequest;
import com.shiyi.utils.RedisCache;
import com.shiyi.config.security.login.AdminAuthenticationFailureHandler;
import com.shiyi.config.security.login.AdminAuthenticationSuccessHandler;
import com.shiyi.config.security.login.CusAuthenticationManager;
import com.shiyi.entity.User;
import com.shiyi.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <p> 自定义用户密码校验过滤器 </p>
 *
 * @author : by blue
 * @description :
 * @date : 2019/10/12 15:32
 */
@Slf4j
@Component
public class AdminAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RedisCache redisCache;

    /**
     * @param authenticationManager:             认证管理器
     * @param adminAuthenticationSuccessHandler: 认证成功处理
     * @param adminAuthenticationFailureHandler: 认证失败处理
     */
    public AdminAuthenticationProcessingFilter(CusAuthenticationManager authenticationManager, AdminAuthenticationSuccessHandler adminAuthenticationSuccessHandler, AdminAuthenticationFailureHandler adminAuthenticationFailureHandler) {
        // 代码使用json传递登录参数
        super(new AntPathRequestMatcher("/login", "POST"));
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler(adminAuthenticationSuccessHandler);
        this.setAuthenticationFailureHandler(adminAuthenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().contains(Constants.REQUEST_HEADERS_CONTENT_TYPE)) {
            throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
        }

        UsernamePasswordAuthenticationToken authRequest;
        try {
            MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);

            BufferedReader streamReader = new BufferedReader( new InputStreamReader(wrappedRequest.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

         /*   LoginUserDTO loginUserDTO =  JSONObject.parseObject(responseStrBuilder.toString(), LoginUserDTO.class);
            String uuid = loginUserDTO.getUuid();
            String code = loginUserDTO.getCode();
            if(StringUtils.isEmpty(uuid)||StringUtils.isEmpty(code)){
                throw new AuthenticationServiceException(ErrorCode.FAIL_VERIFY_CODE.getMsg());
            }
            String verifyKey = RedisConstants.CAPTCHA_CODE + uuid;
            String captcha = redisCache.getCacheObject(verifyKey);
            // 如果验证码不存在，则提示已过期
            if(captcha==null){
                throw new AuthenticationServiceException(ErrorCode.EXPIRE_VERIFY_CODE.getMsg());
            }
            // 验证码错误，提醒验证码错误
            if(!code.equalsIgnoreCase(captcha)){
                throw new AuthenticationServiceException(ErrorCode.FAIL_VERIFY_CODE.getMsg());
            }*/

            // 将前端传递的数据转换成jsonBean数据格式
            User user = JSONObject.parseObject(wrappedRequest.getBodyJsonStrByJson(wrappedRequest), User.class);
            authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
