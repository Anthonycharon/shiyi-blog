package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult emailRegister(EmailRegisterVO emailRegisterVO);

    ResponseResult updatePassword(EmailRegisterVO emailRegisterVO);

    ResponseResult emailLogin(EmailLoginVO emailLoginVO);

    ResponseResult qqLogin(QQLoginVO qqLoginVO);

    ResponseResult weiboLogin(String code);

    ResponseResult giteeLogin(String code);

    ResponseResult sendEmailCode(String email);

    ResponseResult bindEmail(UserAuthVO vo);

    ResponseResult updateUser(UserAuthVO vo);

}
