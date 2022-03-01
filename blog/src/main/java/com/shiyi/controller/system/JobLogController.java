package com.shiyi.controller.system;


import com.shiyi.service.JobLogService;
import com.shiyi.common.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class JobLogController {

    @Autowired
    private JobLogService jobLogService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "定时任务日志列表", httpMethod = "GET", response = ApiResult.class, notes = "定时任务日志列表")
    public ApiResult query(String jobName,String jobGroup,String status,String startTime,
                           String endTime,Long jobId,Integer pageNo, Integer pageSize) {
        return jobLogService.listData(jobName,jobGroup,status,startTime,endTime,jobId,pageNo,pageSize);
    }

    @PostMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除日志列表", httpMethod = "POST", response = ApiResult.class, notes = "批量删除日志列表")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteBatch(ids);
    }

    @GetMapping(value = "/clean")
    @ApiOperation(value = "清空日志列表", httpMethod = "GET", response = ApiResult.class, notes = "清空日志列表")
    public ApiResult clean() {
        return jobLogService.clean();
    }
}

