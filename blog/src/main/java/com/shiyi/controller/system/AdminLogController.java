package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.AdminLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/system/adminLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "操作日志管理")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "操作日志列表", httpMethod = "GET", response = ApiResult.class, notes = "操作日志列表")
    public ApiResult query(Integer pageNo, Integer pageSize) {
        return adminLogService.listLog(pageNo,pageSize);
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "删除操作日志")
    @SaCheckPermission("/system/adminLog/delete")
    @ApiOperation(value = "删除操作日志", httpMethod = "DELETE", response = ApiResult.class, notes = "删除操作日志")
    public ApiResult delete(@RequestBody List<Long> ids) {
        return adminLogService.delete(ids);
    }
}

