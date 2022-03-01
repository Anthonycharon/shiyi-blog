package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.ExceptionLog;
import com.shiyi.mapper.ExceptionLogMapper;
import com.shiyi.service.ExceptionLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-11
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog> implements ExceptionLogService {

    @Override
    public ApiResult listLog(Integer pageNo, Integer pageSize) {
        QueryWrapper<ExceptionLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(SqlConf.CREATE_TIME);
        Page<ExceptionLog> pg = new Page<>(pageNo, pageSize);
        Page<ExceptionLog> sysLogPage = baseMapper.selectPage(pg, queryWrapper);
        return ApiResult.success(sysLogPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除操作日志失败");
    }
}
