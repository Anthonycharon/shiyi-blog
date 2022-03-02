package com.shiyi.config.security.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.dto.UserDetailDTO;
import com.shiyi.common.Constants;
import com.shiyi.common.RedisConstants;
import com.shiyi.entity.UserAuth;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.dto.SecurityUser;
import com.shiyi.entity.Role;
import com.shiyi.entity.User;
import com.shiyi.entity.UserRole;
import com.shiyi.mapper.UserRoleMapper;
import com.shiyi.utils.IpUtils;
import com.shiyi.utils.RedisCache;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p> 自定义userDetailsService - 认证用户详情 </p>
 *
 * @author : by blue
 * @description :
 * @date : 2019/10/14 17:46
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisCache redisCache;

    /***
     * 根据账号获取用户信息
     * @param username:
     * @return: org.springframework.security.core.userdetails.UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        List<User> userList = userMapper.selectList(new QueryWrapper<User>().eq("username", username));
        User user;
        // 判断用户是否存在
        if (!CollectionUtils.isEmpty(userList)) {
            user = userList.get(0);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误，请重试！");
        }
        if (user.getStatus() == Constants.USER_STATUS_ZERO) throw new UsernameNotFoundException("该用户已被禁用!!");
        // 返回UserDetails实现类
        return new SecurityUser(user, getUserRoles(user.getId()));
    }

    /***
     * 根据token获取用户权限与基本信息
     *
     * @param token:
     * @return: com.by zq.config.security.dto.SecurityUser
     */
    public SecurityUser getUserByToken(String token) {
        User user = null;
        List<User> loginList = userMapper.selectList(new QueryWrapper<User>().eq("token", token));
        if (!CollectionUtils.isEmpty(loginList)) {
            user = loginList.get(0);
        }
        return user != null ? new SecurityUser(user, getUserRoles(user.getId())) : null;
    }

    /***
     * 根据token获取用户权限与基本信息
     *
     * @param name:
     * @return: com.by zq.config.security.dto.SecurityUser
     */
    public SecurityUser getUserByName(String name) {
        User user = null;
        List<User> loginList = userMapper.selectList(new QueryWrapper<User>().eq("username", name));
        if (!CollectionUtils.isEmpty(loginList)) {
            user = loginList.get(0);
        }
        return user != null ? new SecurityUser(user, getUserRoles(user.getId())) : null;
    }

    /**
     * 根据用户id获取角色权限信息
     *
     * @param userId
     * @return
     */
    private List<Role> getUserRoles(Integer userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        List<Role> roleList = new LinkedList<>();
        for (UserRole userRole : userRoles) {
            Role role = roleMapper.selectById(userRole.getRoleId());
            roleList.add(role);
        }
        return roleList;
    }
    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserDetailDTO convertUserDetail(User user, HttpServletRequest request) {
        // 查询账号信息
        UserAuth userAuth = userAuthMapper.selectById(user.getUserAuthId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisCache.sMembers(RedisConstants.ARTICLE_USER_LIKE + userAuth.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
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
                .isDisable(userAuth.getIsDisable())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }
}
