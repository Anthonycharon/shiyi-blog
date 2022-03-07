package com.shiyi.utils;

import com.shiyi.dto.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author blue
 * @date 2022/3/7
 * @apiNote
 */
public class UserUtil {

    public static Integer getUserId(){
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getCurrentUserInfo().getId();
    }
}
