package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.UserLog;
import com.shiyi.mapper.UserLogMapper;
import com.shiyi.service.UserLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@Service
public class LogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

    /**
     * 用户日志列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(Integer pageNo, Integer pageSize) {
        QueryWrapper<UserLog> queryWrapper = new QueryWrapper<UserLog>()
                .orderByDesc(SqlConf.CREATE_TIME);
        Page<UserLog> sysLogPage = baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return ApiResult.success(sysLogPage);
    }

    /**
     * 批量删除用户日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除用户日志失败");
    }
}
