package com.shiyi.config.security.login;


import com.shiyi.config.security.service.impl.UserDetailsServiceImpl;
import com.shiyi.mapper.UserMapper;
import com.shiyi.dto.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *  <p> 自定义认证处理 </p>
 *
 * @description :
 * @author : by blue
 * @date : 2019/10/12 14:49
 */
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取前端表单中输入后返回的用户名、密码
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        SecurityUser userInfo = (SecurityUser) userDetailsService.loadUserByUsername(userName);

        boolean isValid = passwordEncoder.matches(password,userInfo.getPassword());
        // 验证密码
        if (!isValid) {
            throw new BadCredentialsException("密码错误！");
        }
        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
