package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.UserLogService;
import io.swagger.annotations.ApiOperation;
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
public class UserLogController {

    @Autowired
    private UserLogService userLogService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "用户日志列表", httpMethod = "GET", response = ApiResult.class, notes = "用户日志列表")
    public ApiResult query(Integer pageNo, Integer pageSize) {
        return userLogService.listData(pageNo,pageSize);
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "删除用户日志")
    @ApiOperation(value = "删除用户日志", httpMethod = "DELETE", response = ApiResult.class, notes = "删除用户日志")
    public ApiResult delete(@RequestBody List<Long> ids) {
        return userLogService.delete(ids);
    }
}

