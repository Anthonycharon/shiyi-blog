package com.shiyi.controller.api;


import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.service.CategoryService;
import com.shiyi.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/web/tags")
@Api(tags = "标签接口")
public class ApiTagsController {
    @Autowired
    private TagsService tagsService;
    @Autowired
    private CategoryService categoryService;

    @BusinessLog(value = "标签模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = ApiResult.class, notes = "标签列表")
    public ApiResult query(){
        return tagsService.webList();
    }

    @BusinessLog(value = "分类模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/categoryList",method = RequestMethod.GET)
    @ApiOperation(value = "分类列表", httpMethod = "GET", response = ApiResult.class, notes = "分类列表")
    public ApiResult categoryList(){
        return categoryService.webList();
    }


}

