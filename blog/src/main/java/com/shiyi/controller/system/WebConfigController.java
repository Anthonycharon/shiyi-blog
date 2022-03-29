package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.WebConfig;
import com.shiyi.service.WebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 网站配置表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
@RestController
@RequestMapping("/system/webConfig")
@Api(tags = "网站配置管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebConfigController {

    private final WebConfigService webConfigService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "网站配置列表", httpMethod = "GET", response = ApiResult.class, notes = "网站配置列表")
    public ApiResult list() {
        return webConfigService.listData();
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/webConfig/update")
    @ApiOperation(value = "修改网站配置", httpMethod = "POST", response = ApiResult.class, notes = "网站配置列表")
    @OperationLogger(value = "修改网站配置")
    public ApiResult update(@RequestBody WebConfig webConfig) {
        return webConfigService.updateData(webConfig);
    }
}

