package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.AdminLog;
import com.shiyi.mapper.AdminLogMapper;
import com.shiyi.service.AdminLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog> implements AdminLogService {

    @Override
    public ApiResult listLog() {
        QueryWrapper<AdminLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SqlConf.CREATE_TIME);
        Page<AdminLog> pg = new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize());
        Page<AdminLog> sysLogPage = baseMapper.selectPage(pg, queryWrapper);
        return ApiResult.success(sysLogPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除操作日志失败");
    }
}
