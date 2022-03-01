package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Dict;
import com.shiyi.service.DictService;
import com.shiyi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/dict")
@Api(tags = "字典类型管理")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "字典类型列表", httpMethod = "GET", response = ApiResult.class, notes = "字典类型列表")
    public ApiResult list(String name,Integer isPublish,
                          String descColumn,String ascColumn,int pageNo,int pageSize){
        return dictService.listData(name,isPublish,descColumn,ascColumn,pageNo,pageSize);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加字典", httpMethod = "POST", response = ApiResult.class, notes = "添加字典")
    @OperationLogger(value = "添加字典")
    public ApiResult save(@RequestBody Dict dict){
        return dictService.addSysDict(dict);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改字典", httpMethod = "POST", response = ApiResult.class, notes = "修改字典")
    @OperationLogger(value = "修改字典")
    public ApiResult update(@RequestBody Dict dict){
        return dictService.updateSysDict(dict);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除字典", httpMethod = "DELETE", response = ApiResult.class, notes = "删除字典")
    @OperationLogger(value = "删除字典")
    public ApiResult delete(int id){
        return dictService.delete(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除字典", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除字典")
    @OperationLogger(value = "批量删除字典")
    public ApiResult deleteBatch(@RequestBody List<Long> list){
        return dictService.deleteBatch(list);
    }
}

