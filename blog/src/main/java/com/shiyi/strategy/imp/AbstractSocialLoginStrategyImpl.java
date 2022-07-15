package com.shiyi.strategy.imp;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shiyi.dto.SocialTokenDTO;
import com.shiyi.dto.SocialUserInfoDTO;
import com.shiyi.dto.UserDetailDTO;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.common.RedisConstants;
import com.shiyi.entity.Role;
import com.shiyi.entity.User;
import com.shiyi.entity.UserAuth;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.strategy.SocialLoginStrategy;
import com.shiyi.utils.*;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.shiyi.common.ResultCode.DISABLE_ACCOUNT;
import static com.shiyi.enums.UserStatusEnum.disable;

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
    @Resource
    private HttpServletRequest request;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserInfoDTO login(String data) {
        // 创建登录信息
        UserDetailDTO userDetailDTO;
        // 获取第三方token信息
        SocialTokenDTO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getCityInfo(ipAddress);
        // 获取第三方用户信息
        SocialUserInfoDTO socialUserInfo = getSocialUserInfo(socialToken);
        if (socialToken.getLoginType().equals(LoginTypeEnum.GITEE.getType())){
            socialToken.setOpenId(socialUserInfo.getId());
        }
        // 判断是否已注册
        User user = getUser(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailDTO = getUserDetail(user, ipAddress, ipSource,socialUserInfo);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailDTO = saveUserDetail(socialToken, ipAddress, ipSource,socialUserInfo);
        }
        // 判断账号是否禁用
        Assert.isTrue(!userDetailDTO.getIsDisable().equals(disable.code),DISABLE_ACCOUNT.desc);

        // 返回用户信息
        UserInfoDTO userInfoDTO = BeanCopyUtil.copyObject(userDetailDTO, UserInfoDTO.class);
        StpUtil.login(userInfoDTO.getId().longValue());
        userInfoDTO.setToken(StpUtil.getTokenValue());
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
    private UserDetailDTO getUserDetail(User user, String ipAddress, String ipSource,SocialUserInfoDTO socialUserInfo) {
        // 更新登录信息
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getLastLoginTime, LocalDateTime.now())
                .set(User::getIpAddress, ipAddress)
                .set(User::getIpSource, ipSource)
                .eq(User::getId, user.getId()));

        //更新头像和昵称
        userAuthMapper.update(new UserAuth(),new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getAvatar, socialUserInfo.getAvatar())
                .set(UserAuth::getNickname, socialUserInfo.getNickname())
                .eq(UserAuth::getId, user.getUserAuthId()));

        // 封装信息
        return convertUserDetail(user);
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
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(DateUtil.getNowDate())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .roleId(2)
                .build();
        userMapper.insert(user);

        return convertUserDetail(user);
    }

    private UserDetailDTO convertUserDetail(User user) {
        // 查询账号信息
        UserAuth userAuth = userAuthMapper.selectById(user.getUserAuthId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisCache.sMembers(RedisConstants.ARTICLE_USER_LIKE + user.getId());
        // 获取设备信息
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getCityInfo(ipAddress);
        UserAgent userAgent = IpUtil.getUserAgent(request);
        // 查询账号角色
        Role role = roleMapper.selectById(user.getRoleId());
        List<String> roleList = new ArrayList<>();
        roleList.add(role.getCode());
        // 封装权限集合
        return UserDetailDTO.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .userAuthId(userAuth.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userAuth.getEmail())
                .roleList(roleList)
                .nickname(userAuth.getNickname())
                .avatar(userAuth.getAvatar())
                .intro(userAuth.getIntro())
                .webSite(userAuth.getWebSite())
                .articleLikeSet(articleLikeSet)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(user.getStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }
}
