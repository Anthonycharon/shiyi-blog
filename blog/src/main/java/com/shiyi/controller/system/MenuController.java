package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Menu;
import com.shiyi.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author blue
 * @description: 后台系统菜单管理控制器
 * @date 2021/7/30 17:12
 */
@RestController
@RequestMapping("/system/menu")
@Api(tags = "系统菜单管理-接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @SaCheckLogin
    @ApiOperation(value = "获取菜单树", httpMethod = "GET", response = ApiResult.class, notes = "获取菜单树")
    public ApiResult getMenuTree() {
        List<Menu> result = menuService.getMenuTree(menuService.list());
        return ApiResult.ok("获取菜单树成功", result);
    }

    @GetMapping(value = "/getMenuApi")
    @SaCheckLogin
    @ApiOperation(value = "获取所有接口", httpMethod = "GET", response = ApiResult.class, notes = "获取所有接口")
    public ApiResult getMenuApi(Integer id) {
         return menuService.getMenuApi(id);
    }

    @PostMapping(value = "/create")
    @SaCheckPermission("/system/menu/create")
    @ApiOperation(value = "添加菜单", httpMethod = "POST", response = ApiResult.class, notes = "添加菜单")
    @OperationLogger(value = "添加菜单")
    public ApiResult create(@RequestBody Menu menu) {
        return menuService.saveMenu(menu);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/menu/update")
    @ApiOperation(value = "修改菜单", httpMethod = "POST", response = ApiResult.class, notes = "修改菜单")
    @OperationLogger(value = "修改菜单")
    public ApiResult update(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/remove")
    @SaCheckPermission("/system/menu/remove")
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE", response = ApiResult.class, notes = "删除菜单")
    @OperationLogger(value = "删除菜单")
    public ApiResult remove(Integer id) {
        return menuService.removeMenu(id);
    }
}
