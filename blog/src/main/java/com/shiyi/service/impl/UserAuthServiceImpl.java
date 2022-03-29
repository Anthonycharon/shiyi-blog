package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.common.*;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.entity.User;
import com.shiyi.entity.UserAuth;
import com.shiyi.entity.WebConfig;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.ErrorCode;
import com.shiyi.mapper.UserAuthMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.service.UserAuthService;
import com.shiyi.service.UserService;
import com.shiyi.service.WebConfigService;
import com.shiyi.strategy.context.SocialLoginStrategyContext;
import com.shiyi.utils.*;
import com.shiyi.vo.EmailLoginVO;
import com.shiyi.vo.EmailRegisterVO;
import com.shiyi.vo.QQLoginVO;
import com.shiyi.vo.UserAuthVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    private final SocialLoginStrategyContext socialLoginStrategyContext;

    private final EmailUtil emailUtil;

    private final UserService userService;

    private final RedisCache redisCache;

    private final WebConfigService webConfigService;

    /**
     * 邮箱账号注册
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult emailRegister(EmailRegisterVO vo) {

        checkEmail(vo.getEmail());

        checkCode(RedisConstants.EMAIL_CODE + vo.getEmail(), vo.getCode());

        User user = getByUserName(vo.getEmail());
        Assert.isNull(user,"该邮箱已注册，请直接登录!");

        WebConfig config = webConfigService.getOne(new QueryWrapper<WebConfig>().last(SysConf.LIMIT_ONE));
        UserAuth auth = UserAuth.builder().email(vo.getEmail()).avatar(config.getTouristAvatar()).nickname(vo.getNickname()).build();
        baseMapper.insert(auth);

        user = User.builder().username(vo.getEmail()).roleId(Constants.USER_ROLE_ID).userAuthId(auth.getId()).loginType(LoginTypeEnum.EMAIL.getType())
                .password(PasswordUtils.aesEncrypt(vo.getPassword())).build();
        boolean insert = userService.save(user);

        redisCache.deleteObject(RedisConstants.EMAIL_CODE + vo.getEmail());

        return insert  ? ApiResult.ok("注册成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 修改密码
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePassword(EmailRegisterVO vo) {

        checkEmail(vo.getEmail());
        checkCode(RedisConstants.EMAIL_CODE + vo.getEmail(), vo.getCode());

        User user = getByUserName(vo.getEmail());
        Assert.notNull(user,ErrorCode.ERROR_MUST_REGISTER.getMsg());

        user.setPassword(PasswordUtils.aesEncrypt(vo.getPassword()));
        boolean update = userService.updateById(user);

        redisCache.deleteObject(RedisConstants.EMAIL_CODE + vo.getEmail());

        return update  ? ApiResult.ok("修改成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 邮箱登录
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult emailLogin(EmailLoginVO vo) {

        checkEmail(vo.getEmail());

        String password = vo.getPassword();
        Assert.isTrue(StringUtils.isNotBlank(password),"密码格式不合法!!");

        User user = getByUserName(vo.getEmail());
        Assert.notNull(user,ErrorCode.ERROR_MUST_REGISTER.getMsg());
        Assert.isTrue(user.getStatus() == Constants.USER_STATUS_ONE,"该邮箱账号已被管理员禁止登录！！");

        boolean validate = PasswordUtils.isValidPassword(user.getPassword(),vo.getPassword());
        Assert.isTrue(validate,"密码错误!!");

        UserAuth auth = baseMapper.selectById(user.getUserAuthId());

        //登录
        StpUtil.login(user.getId().longValue());

        //组装数据
        UserInfoDTO userInfoDTO = UserInfoDTO.builder().id(user.getId()).userInfoId(auth.getId()).avatar(auth.getAvatar()).nickname(auth.getNickname())
                .intro(auth.getIntro()).webSite(auth.getWebSite()).email(user.getUsername()).loginType(user.getLoginType()).token(StpUtil.getTokenValue()).build();

        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult qqLogin(QQLoginVO qqLoginVO) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult weiboLogin(String code) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(code, LoginTypeEnum.WEIBO);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult giteeLogin(String code) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(code, LoginTypeEnum.GITEE);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult logout() {
        StpUtil.logout();
        return ApiResult.ok("退出成功");
    }

    /**
     * 用户绑定邮箱，发送验证码
     * @param email
     * @return
     */
    @Override
    public ApiResult sendEmailCode(String email) {
        try {
            emailUtil.sendCode(email);
            return ApiResult.ok("验证码已发送，请前往邮箱查看!!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ApiResult.ok(ErrorCode.ERROR_DEFAULT.getMsg());
        }

    }

    /**
     * 绑定邮箱
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult bindEmail(UserAuthVO vo) {
        String key = RedisConstants.EMAIL_CODE + vo.getEmail();
        checkCode(key, vo.getCode());

        UserAuth userAuth = getUserAuth();
        userAuth.setEmail(vo.getEmail());
        boolean update = updateById(userAuth);
        redisCache.deleteObject(key);
        return update ? ApiResult.ok("绑定邮箱成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateUser(UserAuthVO vo) {
        UserAuth userAuth = getUserAuth();
        userAuth.setNickname(vo.getNickname());
        userAuth.setWebSite(vo.getWebSite());
        userAuth.setEmail(vo.getEmail());
        userAuth.setIntro(vo.getIntro());
        userAuth.setAvatar(vo.getAvatar());

        boolean update = updateById(userAuth);
        return update ? ApiResult.ok("修改信息成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }


    //---------------自定义方法开始-------------
    private UserAuth getUserAuth() {
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        return baseMapper.selectById(user.getUserAuthId());
    }

    public void checkEmail(String email){
        boolean matches = Pattern.compile("\\w+@{1}\\w+\\.{1}\\w+").matcher(email).matches();
        Assert.isTrue(matches,ErrorCode.EMAIL_ERROR.getMsg());
    }

    public User getByUserName(String username){
        return userService.getOne(new QueryWrapper<User>().eq(SqlConf.USERNAME, username));
    }

    private void checkCode(String key, String sourCode) {
        Object code = redisCache.getCacheObject(key);
        Assert.isTrue(code != null && code.equals(sourCode), ErrorCode.ERROR_EXCEPTION_MOBILE_CODE.getMsg());
    }
}
