package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Job;
import com.shiyi.enums.TaskException;
import com.shiyi.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "定时任务列表", httpMethod = "GET", response = ApiResult.class, notes = "定时任务列表")
    public ApiResult query(String jobName,String jobGroup,String status) {
        return jobService.listData(jobName,jobGroup,status);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/job/info")
    @ApiOperation(value = "定时任务详情", httpMethod = "GET", response = ApiResult.class, notes = "定时任务详情")
    public ApiResult info(Long jobId) {
        return jobService.info(jobId);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/job/add")
    @ApiOperation(value = "添加定时任务", httpMethod = "POST", response = ApiResult.class, notes = "添加定时任务")
    @OperationLogger(value = "添加定时任务")
    public ApiResult add(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.addJob(job);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/job/update")
    @ApiOperation(value = "修改定时任务", httpMethod = "POST", response = ApiResult.class, notes = "修改定时任务")
    @OperationLogger(value = "修改定时任务")
    public ApiResult update(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(job);
    }

    @GetMapping(value = "/delete")
    @SaCheckPermission("/system/job/delete")
    @ApiOperation(value = "删除定时任务", httpMethod = "GET", response = ApiResult.class, notes = "删除定时任务")
    @OperationLogger(value = "删除定时任务")
    public ApiResult delete(Long jobId) throws SchedulerException, TaskException {
        return jobService.deleteJob(jobId);
    }
    @PostMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/job/deleteBatch")
    @ApiOperation(value = "批量删除定时任务", httpMethod = "POST", response = ApiResult.class, notes = "批量删除定时任务")
    @OperationLogger(value = "批量删除定时任务")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) throws SchedulerException, TaskException {
        return jobService.deleteBatch(ids);
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("/system/job/run")
    @ApiOperation(value = "立即执行", httpMethod = "POST", response = ApiResult.class, notes = "立即执行")
    @OperationLogger(value = "立即执行")
    public ApiResult run(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.run(job);
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("/system/job/change")
    @ApiOperation(value = "修改状态", httpMethod = "POST", response = ApiResult.class, notes = "修改状态")
    @OperationLogger(value = "修改状态")
    public ApiResult changeStatus(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.changeStatus(job);
    }
}

