package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.ExceptionLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-11
 */
@RestController
@RequestMapping("/system/exceptionLog")
public class ExceptionLogController {

    @Autowired
    private ExceptionLogService exceptionLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "异常日志列表", httpMethod = "GET", response = ApiResult.class, notes = "异常日志列表")
    public ApiResult query(Integer pageNo, Integer pageSize) {
        return exceptionLogService.listLog(pageNo,pageSize);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/exceptionLog/delete")
    @OperationLogger(value = "删除异常日志")
    @ApiOperation(value = "删除异常日志", httpMethod = "DELETE", response = ApiResult.class, notes = "删除异常日志")
    public ApiResult delete(@RequestBody List<Long> ids) {
        return exceptionLogService.delete(ids);
    }
}

