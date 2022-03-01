package com.shiyi.service;

import com.shiyi.entity.Job;
import com.shiyi.common.ApiResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.enums.TaskException;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
public interface JobService extends IService<Job> {

    ApiResult listData(String jobName,String jobGroup,String status,Integer pageNo, Integer pageSize);

    ApiResult info(Long jobId);

    ApiResult addJob(Integer userId, Job job) throws SchedulerException, TaskException;

    ApiResult updateJob(Integer userId, Job job) throws SchedulerException, TaskException;

    ApiResult deleteJob(Long jobId) throws SchedulerException;

    ApiResult deleteBatch(List<Long> ids);

    ApiResult pauseJob(Job job) throws SchedulerException ;

    ApiResult run(Job job) throws SchedulerException;

    ApiResult changeStatus(Integer userId, Job job) throws SchedulerException;

}
