package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.FeedBackService;
import io.swagger.annotations.Api;
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
 * @since 2022-01-13
 */
@RestController
@RequestMapping("/system/feedback")
@Api(tags = "后台反馈管理")
public class FeedBackController {

    @Autowired
    private FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "反馈列表", httpMethod = "GET", response = ApiResult.class, notes = "反馈列表")
    public ApiResult list(Integer type,Integer pageNo, Integer pageSize) {
        return feedBackService.listData(type,pageNo,pageSize);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/feedback/deleteBatch")
    @ApiOperation(value = "删除反馈", httpMethod = "DELETE", response = ApiResult.class, notes = "删除反馈")
    @OperationLogger(value = "删除反馈")
    public ApiResult delete(@RequestBody List<Integer> ids) {
        return feedBackService.delete(ids);
    }
}

