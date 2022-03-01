package com.shiyi.controller.system;


import com.shiyi.annotation.AuthUserId;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Job;
import com.shiyi.enums.TaskException;
import com.shiyi.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/system/job")
@Api(tags = "定时任务管理")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "定时任务列表", httpMethod = "GET", response = ApiResult.class, notes = "定时任务列表")
    public ApiResult query(String jobName,String jobGroup,String status,Integer pageNo, Integer pageSize) {
        return jobService.listData(jobName,jobGroup,status,pageNo,pageSize);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "定时任务详情", httpMethod = "GET", response = ApiResult.class, notes = "定时任务详情")
    public ApiResult info(Long jobId) {
        return jobService.info(jobId);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加定时任务", httpMethod = "POST", response = ApiResult.class, notes = "添加定时任务")
    @OperationLogger(value = "添加定时任务")
    public ApiResult add(@AuthUserId Integer userId,@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.addJob(userId, job);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改定时任务", httpMethod = "POST", response = ApiResult.class, notes = "修改定时任务")
    @OperationLogger(value = "修改定时任务")
    public ApiResult update(@AuthUserId Integer userId,@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(userId, job);
    }

    @GetMapping(value = "/delete")
    @ApiOperation(value = "删除定时任务", httpMethod = "GET", response = ApiResult.class, notes = "删除定时任务")
    @OperationLogger(value = "删除定时任务")
    public ApiResult delete(Long jobId) throws SchedulerException, TaskException {
        return jobService.deleteJob(jobId);
    }
    @PostMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除定时任务", httpMethod = "POST", response = ApiResult.class, notes = "批量删除定时任务")
    @OperationLogger(value = "批量删除定时任务")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) throws SchedulerException, TaskException {
        return jobService.deleteBatch(ids);
    }

    @PostMapping(value = "/run")
    @ApiOperation(value = "立即执行", httpMethod = "POST", response = ApiResult.class, notes = "立即执行")
    @OperationLogger(value = "立即执行")
    public ApiResult run(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.run(job);
    }

    @PostMapping(value = "/change")
    @ApiOperation(value = "修改状态", httpMethod = "POST", response = ApiResult.class, notes = "修改状态")
    @OperationLogger(value = "修改状态")
    public ApiResult changeStatus(@AuthUserId Integer userId,@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.changeStatus(userId, job);
    }
}

