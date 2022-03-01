package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.vo.EmailLoginVO;
import com.shiyi.vo.EmailRegisterVO;
import com.shiyi.vo.QQLoginVO;
import com.shiyi.vo.UserAuthVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
public interface UserAuthService extends IService<UserAuth> {

    ApiResult emailRegister(EmailRegisterVO emailRegisterVO);

    ApiResult updatePassword(EmailRegisterVO emailRegisterVO);

    ApiResult emailLogin(EmailLoginVO emailLoginVO);

    ApiResult qqLogin(QQLoginVO qqLoginVO);

    ApiResult weiboLogin(String code);

    ApiResult giteeLogin(String code);

    ApiResult logout();

    ApiResult listOnlineUsers(String keywords,int pageNo,int pageSize);

    ApiResult sendEmailCode(String email);

    ApiResult bindEmail(UserAuthVO vo);

    ApiResult updateUser(UserAuthVO vo);

}
