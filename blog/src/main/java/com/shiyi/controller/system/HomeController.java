package com.shiyi.controller.system;


import com.shiyi.common.ApiResult;
import com.shiyi.service.impl.HomeServiceImpl;
import com.shiyi.dto.SystemHardwareInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Api(tags = "后台首页")
public class HomeController {
    @Autowired
    private HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = ApiResult.class, notes = "首页各种统计信息")
    public ApiResult init() {
        return ApiResult.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @ApiOperation(value = "首页文章、ip用户、留言统计", httpMethod = "GET", response = ApiResult.class, notes = "首页文章、ip用户、留言统计")
    public ApiResult lineCount() {
        return ApiResult.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
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
}
