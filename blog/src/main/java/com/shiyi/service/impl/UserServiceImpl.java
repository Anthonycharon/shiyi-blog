package com.shiyi.service.impl;

import cn.dev33.satoken.SaManager;
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
import com.shiyi.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

import static com.shiyi.common.ResultCode.ERROR_USER_NOT_EXIST;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 17:25
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final MenuService menuService;

    private final UserAuthMapper userAuthMapper;

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
        Page<UserDTO> page = baseMapper.selectPageRecord(new Page<>(pageNo, pageSize),username,loginType);
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

    @Override
    public ApiResult listOnlineUsers(String keywords,int pageNo,int pageSize) {

        List<String> sessionIds = StpUtil.searchTokenValue(null, -1, 1000);
        log.info("当前用户：{}", Arrays.toString(sessionIds.toArray()));
        MySaTokenListener.ONLINE_USERS.sort((o1, o2) -> DateUtil.compare(o2.getLoginTime(), o1.getLoginTime()));
        int fromIndex = (pageNo-1) * pageSize;
        int toIndex = MySaTokenListener.ONLINE_USERS.size() - fromIndex > pageSize ? fromIndex + pageSize : MySaTokenListener.ONLINE_USERS.size();
        List<OnlineUser> userOnlineList = MySaTokenListener.ONLINE_USERS.subList(fromIndex, toIndex);
        log.warn("stp用户数：{}，memory用户数：{}", sessionIds.size(), userOnlineList.size());
        userOnlineList.forEach(onlineUser -> {
            String keyLastActivityTime = StpUtil.stpLogic.splicingKeyLastActivityTime(onlineUser.getTokenValue());
            String lastActivityTimeString = SaManager.getSaTokenDao().get(keyLastActivityTime);
            if (lastActivityTimeString != null) {
                long lastActivityTime = Long.parseLong(lastActivityTimeString);
                onlineUser.setLastActivityTime(DateUtil.date(lastActivityTime));
            }
        });
        Map<String,Object> map = new HashMap<>();
        map.put("total",userOnlineList.size());
        map.put("records",userOnlineList);
        return ApiResult.success(map);
    }

    @Override
    public ApiResult kick(Long userId) {
        log.info("当前踢下线的用户id为:{}",userId);
        StpUtil.kickout(userId);
        return ApiResult.ok();
    }
}
