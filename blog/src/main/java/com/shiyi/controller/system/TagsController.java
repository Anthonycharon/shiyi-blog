package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Tags;
import com.shiyi.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/system/tags")
@Api(tags = "标签管理")
public class TagsController {
    @Autowired
    private TagsService tagsService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = ApiResult.class, notes = "标签列表")
    public ApiResult query(String name,Integer pageNo,Integer pageSize){
        return tagsService.listData(name,pageNo,pageSize);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "新增标签", httpMethod = "POST", response = ApiResult.class, notes = "新增标签")
    @OperationLogger(value = "新增标签")
    public ApiResult add(@RequestBody Tags tags){
        return tagsService.addTag(tags);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiOperation(value = "标签详情", httpMethod = "GET", response = ApiResult.class, notes = "标签详情")
    public ApiResult info(@RequestParam(required = true) Long id){
        return tagsService.info(id);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改标签", httpMethod = "POST", response = ApiResult.class, notes = "修改标签")
    @OperationLogger(value = "修改标签")
    public ApiResult update(@RequestBody Tags tags){
        return tagsService.updateTag(tags);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除标签", httpMethod = "DELETE", response = ApiResult.class, notes = "删除标签")
    @OperationLogger(value = "删除标签")
    public ApiResult remove(Long  id){
        return tagsService.delete(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除标签", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除标签")
    @OperationLogger(value = "批量删除标签")
    public ApiResult remove(@RequestBody List<Long> ids){
        return tagsService.deleteBatch(ids);
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @ApiOperation(value = "置顶标签", httpMethod = "GET", response = ApiResult.class, notes = "置顶标签")
    @OperationLogger(value = "置顶标签")
    public ApiResult top(Long id){
        return tagsService.top(id);
    }
}

