package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.SystemConfig;
import com.shiyi.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/config")
@Api(tags = "系统配置管理")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping(value = "/getConfig",method = RequestMethod.GET)
    @ApiOperation(value = "查询系统配置", httpMethod = "GET", response = ApiResult.class, notes = "查询系统配置")
    public ApiResult getConfig(){
        return systemConfigService.getConfig();
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改系统配置", httpMethod = "POST", response = ApiResult.class, notes = "修改系统配置")
    @OperationLogger(value = "修改系统配置")
    public ApiResult update(@RequestBody SystemConfig systemConfig){
        return systemConfigService.updateConfig(systemConfig);
    }
}

