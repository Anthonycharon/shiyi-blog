package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.entity.JobLog;
import com.shiyi.mapper.JobLogMapper;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.service.JobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    /**
     * 任务日志列表
     * @param jobName
     * @param jobGroup
     * @param status
     * @param startTime
     * @param endTime
     * @param jobId
     * @return
     */
    @Override
    public ApiResult listData(String jobName,String jobGroup,String status,String startTime,
                              String endTime,Long jobId) {
        QueryWrapper<JobLog> queryWrapper = new QueryWrapper<JobLog>()
                .orderByDesc(SqlConf.CREATE_TIME).eq(jobId != null,SqlConf.JOB_ID,jobId)
                .like(StringUtils.isNotBlank(jobName),SqlConf.JOB_NAME,jobName)
                .like(StringUtils.isNotBlank(jobGroup),SqlConf.JOB_GROUP,jobGroup)
                .eq(StringUtils.isNotBlank(status),SqlConf.STATUS,status)
                .between(StringUtils.isNotBlank(startTime),SqlConf.START_TIME,startTime,endTime);
        Page<JobLog> page = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        return ApiResult.success(page);
    }

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ApiResult.ok();
    }

    /**
     * 清空日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult clean() {
        baseMapper.clean();
        return ApiResult.ok();
    }

}
