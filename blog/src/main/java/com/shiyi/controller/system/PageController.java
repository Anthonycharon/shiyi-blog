package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Page;
import com.shiyi.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/system/page")
@Api(tags = "后台页面管理")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "页面列表", httpMethod = "GET", response = ApiResult.class, notes = "页面列表")
    public ApiResult query() {
        return pageService.listData();
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增页面", httpMethod = "POST", response = ApiResult.class, notes = "新增页面")
    @OperationLogger(value = "新增页面")
    public ApiResult add(@RequestBody Page page) {
        return pageService.addPage(page);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改页面", httpMethod = "POST", response = ApiResult.class, notes = "修改页面")
    @OperationLogger(value = "修改页面")
    public ApiResult update(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除页面", httpMethod = "DELETE", response = ApiResult.class, notes = "删除页面")
    @OperationLogger(value = "删除页面")
    public ApiResult delete(Long id) {
        return pageService.deletePage(id);
    }
}

