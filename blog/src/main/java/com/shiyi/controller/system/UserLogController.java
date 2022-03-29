package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.UserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
@RestController
@RequestMapping("/system/userLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "用户日志管理")
public class UserLogController {

    private final UserLogService userLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "用户日志列表", httpMethod = "GET", response = ApiResult.class, notes = "用户日志列表")
    public ApiResult query(Integer pageNo, Integer pageSize) {
        return userLogService.listData(pageNo,pageSize);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/userLog/delete")
    @OperationLogger(value = "删除用户日志")
    @ApiOperation(value = "删除用户日志", httpMethod = "DELETE", response = ApiResult.class, notes = "删除用户日志")
    public ApiResult delete(@RequestBody List<Long> ids) {
        return userLogService.delete(ids);
    }
}

