package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.vo.EmailLoginDTO;
import com.shiyi.vo.EmailRegisterDTO;
import com.shiyi.vo.QQLoginDTO;
import com.shiyi.vo.UserAuthDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
public interface UserAuthService extends IService<UserAuth> {

    ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO);

    ResponseResult updatePassword(EmailRegisterDTO emailRegisterDTO);

    ResponseResult emailLogin(EmailLoginDTO emailLoginDTO);

    ResponseResult qqLogin(QQLoginDTO qqLoginDTO);

    ResponseResult weiboLogin(String code);

    ResponseResult giteeLogin(String code);

    ResponseResult sendEmailCode(String email);

    ResponseResult bindEmail(UserAuthDTO vo);

    ResponseResult updateUser(UserAuthDTO vo);

}
