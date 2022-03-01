package com.shiyi.aspect.auth;

import com.shiyi.annotation.AuthUserId;
import com.shiyi.dto.SecurityUser;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.common.Constants;
import com.shiyi.service.UserService;
import com.shiyi.utils.SpringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author blue
 * @date 2021/12/9
 * @apiNote
 */
public class UserIdResolver  implements HandlerMethodArgumentResolver {


    /**
     * 判断是否是注解类，返回true则调用resolveArgument方法
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(AuthUserId.class);
    }

    /**
     * 对@AuthUserId进行业务处理
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getCurrentUserInfo().getId();
    }
}
