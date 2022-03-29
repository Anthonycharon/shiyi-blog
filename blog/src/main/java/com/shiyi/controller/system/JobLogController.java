package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.service.JobLogService;
import com.shiyi.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/system/jobLog")
@Api(tags = "定时任务调度日志管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobLogController {

    private final JobLogService jobLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "定时任务日志列表", httpMethod = "GET", response = ApiResult.class, notes = "定时任务日志列表")
    public ApiResult query(String jobName,String jobGroup,String status,String startTime,
                           String endTime,Long jobId,Integer pageNo, Integer pageSize) {
        return jobLogService.listData(jobName,jobGroup,status,startTime,endTime,jobId,pageNo,pageSize);
    }

    @PostMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/jobLog/deleteBatch")
    @ApiOperation(value = "批量删除日志列表", httpMethod = "POST", response = ApiResult.class, notes = "批量删除日志列表")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteBatch(ids);
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("/system/jobLog/clean")
    @ApiOperation(value = "清空日志列表", httpMethod = "GET", response = ApiResult.class, notes = "清空日志列表")
    public ApiResult clean() {
        return jobLogService.clean();
    }
}

