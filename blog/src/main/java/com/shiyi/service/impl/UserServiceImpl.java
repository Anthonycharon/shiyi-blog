package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ApiResult;
import com.shiyi.common.Constants;
import com.shiyi.config.satoken.MySaTokenListener;
import com.shiyi.config.satoken.OnlineUser;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.dto.UserDTO;
import com.shiyi.entity.Menu;
import com.shiyi.entity.User;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.MenuService;
import com.shiyi.service.UserService;
import com.shiyi.utils.PageUtils;
import com.shiyi.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static com.shiyi.common.ResultCode.ERROR_USER_NOT_EXIST;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final MenuService menuService;

    private final UserAuthMapper userAuthMapper;


    /**
     * 用户列表
     * @param username
     * @param loginType
     * @return
     */
    @Override
    public ApiResult listData(String username,Integer loginType) {
        Page<UserDTO> page = baseMapper.selectPageRecord(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),username,loginType);
        return ApiResult.success(page);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @Override
    public ApiResult info(Integer id) {
        SystemUserDTO user = baseMapper.getById(id);
        return ApiResult.success(user);
    }

    /**
     *  添加用户
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult saveUser(User user) {
        user.setPassword(PasswordUtils.aesEncrypt(user.getPassword()));
        user.setStatus(Constants.USER_STATUS_ONE);
        baseMapper.insert(user);
       // roleMapper.insertToUserId(user.getId(),user.getRoleId());
        return ApiResult.success(user);
    }

    /**
     *  修改用户
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateUser(User user) {
        baseMapper.updateById(user);
        //roleMapper.updateByUserId(user.getId(),user.getRoleId());
        return ApiResult.ok();
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Integer> ids) {
        userAuthMapper.deleteByUserIds(ids);
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0?ApiResult.ok():ApiResult.fail("删除失败");
    }

    /**
     * 获取当前登录用户详情
     * @return
     */
    @Override
    public SystemUserDTO getCurrentUserInfo() {
        return baseMapper.getById(StpUtil.getLoginIdAsInt());
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     * @return
     */
    @Override
    public ApiResult getUserMenu() {
        List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsInt());
        List<Menu> menus = menuService.listByIds(menuIds);
        List<Menu> menuTree = menuService.getMenuTree(menus);
        return ApiResult.success(menuTree);
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePassword(Map<String, String> map) {

        User user = baseMapper.selectById(StpUtil.getLoginIdAsInt());
        Assert.notNull(user,ERROR_USER_NOT_EXIST.getDesc());

        boolean isValid = PasswordUtils.isValidPassword(map.get("oldPassword"), user.getPassword());
        Assert.isTrue(isValid,"旧密码校验不通过!");

        String newPassword = PasswordUtils.aesEncrypt(map.get("newPassword"));
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ApiResult.ok("修改成功");
    }

    /**
     * 在线用户
     * @param keywords
     * @return
     */
    @Override
    public ApiResult listOnlineUsers(String keywords) {
        int pageNo = PageUtils.getPageNo().intValue();
        int pageSize = PageUtils.getPageSize().intValue();

        List<OnlineUser> onlineUsers = MySaTokenListener.ONLINE_USERS;
        //根据关键词过滤
        if (StringUtils.isNotBlank(keywords)) {
            onlineUsers = MySaTokenListener.ONLINE_USERS.stream().filter(item -> item.getNickname().contains(keywords)).collect(Collectors.toList());
        }
        //排序
        onlineUsers.sort((o1, o2) -> DateUtil.compare(o2.getLoginTime(), o1.getLoginTime()));
        int fromIndex = (pageNo-1) * pageSize;
        int toIndex = onlineUsers.size() - fromIndex > pageSize ? fromIndex + pageSize : onlineUsers.size();
        List<OnlineUser> userOnlineList = onlineUsers.subList(fromIndex, toIndex);
        logger.info("memory用户数：{}", userOnlineList.size());

        Map<String,Object> map = new HashMap<>();
        map.put("total",onlineUsers.size());
        map.put("records",userOnlineList);
        return ApiResult.success(map);
    }

    /**
     * 踢人下线
     * @param token
     * @return
     */
    @Override
    public ApiResult kick(String token) {
        logger.info("当前踢下线的用户token为:{}",token);
        StpUtil.kickoutByTokenValue(token);
        return ApiResult.ok();
    }
}
