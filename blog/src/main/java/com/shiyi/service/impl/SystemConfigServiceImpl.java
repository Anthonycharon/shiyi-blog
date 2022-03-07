package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SysConf;
import com.shiyi.dto.SecurityUser;
import com.shiyi.entity.SystemConfig;
import com.shiyi.entity.User;
import com.shiyi.mapper.SystemConfigMapper;
import com.shiyi.service.SystemConfigService;
import com.shiyi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Autowired
    private UserService userService;

    /**
     * 获取系统配置
     * @return
     */
    @Override
    public ApiResult getConfig() {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        User user = userService.getById(UserUtil.getUserId());
        if (user.getRoleId() > SysConf.ROLE_ID) queryWrapper.orderByDesc("id");
        queryWrapper.last(SysConf.LIMIT_ONE);
        SystemConfig systemConfig = baseMapper.selectOne(queryWrapper);
        return ApiResult.success(systemConfig);
    }

    /**
     * 修改系统配置
     * @param systemConfig
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateConfig(SystemConfig systemConfig) {
        baseMapper.updateById(systemConfig);
        return ApiResult.ok();
    }

    //---------自定义方法----------
    @Override
    public SystemConfig getCustomizeOne() {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.last(SysConf.LIMIT_ONE);
        return baseMapper.selectOne(queryWrapper);
    }
}
