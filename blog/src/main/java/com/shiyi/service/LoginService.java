package com.shiyi.service;


import com.shiyi.common.ApiResult;
import com.shiyi.vo.LoginVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:58
 */
public interface LoginService {

    Map<String, String> getCode(HttpServletResponse response) throws IOException;


    ApiResult doLogin(LoginVO vo);
}
