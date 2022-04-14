package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.common.ApiResult;
import com.shiyi.service.impl.HomeServiceImpl;
import com.shiyi.dto.SystemHardwareInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system/home")
@Api(tags = "后台首页")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @SaCheckLogin
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = ApiResult.class, notes = "首页各种统计信息")
    public ApiResult init() {
        return ApiResult.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @SaCheckLogin
    @ApiOperation(value = "首页文章、ip用户、留言统计", httpMethod = "GET", response = ApiResult.class, notes = "首页文章、ip用户、留言统计")
    public ApiResult lineCount() {
        return ApiResult.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
    @SaCheckPermission("/system/home/systemInfo")
    @ApiOperation(value = "服务器监控", httpMethod = "GET", response = ApiResult.class, notes = "服务器监控")
    public ApiResult systemInfo() {
        SystemHardwareInfoDTO systemHardwareInfoDTO = new SystemHardwareInfoDTO();
        try {
            systemHardwareInfoDTO.copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.success(systemHardwareInfoDTO);
    }

    @GetMapping(value = "/cache")
    @SaCheckPermission("/system/home/cache")
    @ApiOperation(value = "redis监控", httpMethod = "GET", response = ApiResult.class, notes = "redis监控")
    public ApiResult getCacheInfo(){
        return homeService.getCacheInfo();
    }
}
