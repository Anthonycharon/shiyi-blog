package com.shiyi.strategy.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shiyi.config.security.service.impl.UserDetailsServiceImpl;
import com.shiyi.dto.SocialTokenDTO;
import com.shiyi.dto.SocialUserInfoDTO;
import com.shiyi.dto.UserDetailDTO;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.common.Constants;
import com.shiyi.common.RedisConstants;
import com.shiyi.entity.User;
import com.shiyi.entity.UserAuth;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.strategy.SocialLoginStrategy;
import com.shiyi.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 第三方登录抽象模板
 *
 * @author blue
 * @date 2021/07/28
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisCache redisCache;

    @Override
    public UserInfoDTO login(String data) {
        // 创建登录信息
        UserDetailDTO userDetailDTO;
        // 获取第三方token信息
        SocialTokenDTO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        // 获取第三方用户信息
        SocialUserInfoDTO socialUserInfo = getSocialUserInfo(socialToken);
        if (socialToken.getLoginType().equals(LoginTypeEnum.GITEE.getType())){
            socialToken.setOpenId(socialUserInfo.getId());
        }
        // 判断是否已注册
        User user = getUser(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailDTO = getUserDetail(user, ipAddress, ipSource);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailDTO = saveUserDetail(socialToken, ipAddress, ipSource,socialUserInfo);
        }
        // 判断账号是否禁用
        Assert.isTrue(!userDetailDTO.getIsDisable().equals(Constants.USER_STATUS_ZERO),"账号已被禁用!");

        // 返回用户信息
        UserInfoDTO userInfoDTO = BeanCopyUtils.copyObject(userDetailDTO, UserInfoDTO.class);
        String username = userDetailDTO.getUsername();
        Map<String, Object> claims = new HashMap<>();
        claims.put(RedisConstants.LOGIN_PREFIX,username);
        String token = jwtUtils.generateToken(claims);
        redisCache.deleteObject(RedisConstants.LOGIN_PREFIX+username);
        redisCache.setCacheObject(RedisConstants.LOGIN_PREFIX+username,token,1, TimeUnit.HOURS);
        userInfoDTO.setToken(token);
        return userInfoDTO;
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenDTO} 第三方token信息
     */
    public abstract SocialTokenDTO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenDTO 第三方token信息
     * @return {@link SocialUserInfoDTO} 第三方用户信息
     */
    public abstract SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO);

    /**
     * 获取用户账号
     *
     * @return {@link UserAuth} 用户账号
     */
    private User getUser(SocialTokenDTO socialTokenDTO) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialTokenDTO.getOpenId())
                .eq(User::getLoginType, socialTokenDTO.getLoginType()));
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    private UserDetailDTO getUserDetail(User user, String ipAddress, String ipSource) {
        // 更新登录信息
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getLastLoginTime, LocalDateTime.now())
                .set(User::getIpAddress, ipAddress)
                .set(User::getIpSource, ipSource)
                .eq(User::getId, user.getId()));
        // 封装信息
        return userDetailsService.convertUserDetail(user, request);
    }

    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    private UserDetailDTO saveUserDetail(SocialTokenDTO socialToken, String ipAddress, String ipSource,SocialUserInfoDTO socialUserInfo) {

        // 保存用户信息
        UserAuth userAuth = UserAuth.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .build();
        userAuthMapper.insert(userAuth);
        // 保存账号信息
        User user = User.builder()
                .userAuthId(userAuth.getId())
                .username(socialToken.getOpenId())
                .nickName(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(DateUtils.getNowDate())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .roleId(2)
                .build();
        userMapper.insert(user);

        return userDetailsService.convertUserDetail(user, request);
    }

}
