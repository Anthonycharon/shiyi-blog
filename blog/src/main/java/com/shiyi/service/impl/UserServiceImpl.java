package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ApiResult;
import com.shiyi.common.Constants;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.SqlConf;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.dto.UserDTO;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.entity.Menu;
import com.shiyi.entity.User;
import com.shiyi.exception.ErrorCode;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.MenuService;
import com.shiyi.service.UserService;
import com.shiyi.utils.JwtUtils;
import com.shiyi.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    MenuService menuService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;

    /**
     * 用户列表
     * @param username
     * @param loginType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(String username,Integer loginType, Integer pageNo, Integer pageSize) {
     /*   LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId,User::getAvatar,User::getRoleId,User::getCreateTime,User::getStatus,
                        User::getUpdateTime,User::getLoginType,User::getNickName,User::getLastLoginTime,
                        User::getIpSource,User::getIpAddress).orderByAsc(User::getCreateTime)
                .like(StringUtils.isNotBlank(username),User::getUsername,username)
                .eq(loginType != null,User::getLoginType,loginType);

        Page<User> page = baseMapper.selectPage(new Page<>(pageNo, pageSize),queryWrapper);*/

        Page<UserDTO> page = baseMapper.selectPageRecord(new Page<UserDTO>(pageNo, pageSize),username,loginType);
        return ApiResult.success(page);
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @Override
    public ApiResult info(Integer id) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId,User::getLoginType,User::getRoleId,User::getStatus).eq(User::getId,id));
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Constants.USER_STATUS_ONE);
        baseMapper.insert(user);
        roleMapper.insertToUserId(user.getId(),user.getRoleId());
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
        roleMapper.updateByUserId(user.getId(),user.getRoleId());
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
     * 退出登录
     * @param token
     * @return
     */
    @Override
    public ApiResult logout(String token) {
        try {
            String userName = jwtUtils.getUsernameFromToken(token);
            redisCache.deleteObject(RedisConstants.LOGIN_PREFIX + userName);
            return ApiResult.ok("退出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("退出失败");
        }
    }

    /**
     * 获取当前登录用户详情
     * @param token
     * @return
     */
    @Override
    public SystemUserDTO getCurrentUserInfo(String token) {
        String userName = jwtUtils.getUsernameFromToken(token);
        String userToken = redisCache.getCacheObject(RedisConstants.LOGIN_PREFIX + userName);
        Assert.isTrue(!token.equals(userToken),ErrorCode.EXPIRE_TOKEN.getMsg());
        User user = baseMapper.getOne(userName);
        SystemUserDTO systemUserDTO = new SystemUserDTO();
        BeanUtils.copyProperties(user, systemUserDTO);
        return systemUserDTO;
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     * @param token
     * @return
     */
    @Override
    public ApiResult getUserMenu(String token) {
        String userName = jwtUtils.getUsernameFromToken(token);
        List<Integer> menuIds = baseMapper.getMenuId(userName);
        List<Menu> menus = menuService.listByIds(menuIds);
        List<Menu> menuTree = menuService.getMenuTree(menus);
        return ApiResult.success(menuTree);
    }

    /**
     * 修改密码
     * @param header
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePassword(String header, Map<String, String> map) {
        String userName = jwtUtils.getUsernameFromToken(header);

        User user = baseMapper.selectOne(new QueryWrapper<User>()
                .eq(SqlConf.USERNAME,userName));
        Assert.notNull(user,"未找到该用户");

        boolean isValid = passwordEncoder.matches(map.get("oldPassword"), user.getPassword());
        Assert.isTrue(isValid,"旧密码校验不通过!");

        String newPassword = map.get("newPassword");
        newPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ApiResult.ok("修改成功");
    }
}
