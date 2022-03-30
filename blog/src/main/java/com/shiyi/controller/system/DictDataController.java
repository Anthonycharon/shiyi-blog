package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.service.DictDataService;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.DictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典数据表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/system/dictData")
@Api(tags = "字典数据管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictDataController {

    private final DictDataService dictDataService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "字典数据列表", httpMethod = "GET", response = ApiResult.class, notes = "字典数据列表")
    public ApiResult list(Integer dictId, Integer isPublish){
        return dictDataService.listDictData(dictId,isPublish);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SaCheckPermission("/system/dictData/add")
    @ApiOperation(value = "添加字典数据", httpMethod = "POST", response = ApiResult.class, notes = "添加字典数据")
    @OperationLogger(value = "添加字典数据")
    public ApiResult add(@RequestBody DictData dictData){
        return dictDataService.addDictData(dictData);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SaCheckPermission("/system/dictData/update")
    @ApiOperation(value = "修改字典数据", httpMethod = "POST", response = ApiResult.class, notes = "修改字典数据")
    @OperationLogger(value = "修改字典数据")
    public ApiResult update(@RequestBody DictData dictData){
        return dictDataService.updateDictData(dictData);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/dictData/delete")
    @ApiOperation(value = "删除字典数据", httpMethod = "DELETE", response = ApiResult.class, notes = "删除字典数据")
    @OperationLogger(value = "删除字典数据")
    public ApiResult delete(Long id){
        return dictDataService.delete(id);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/dictData/deleteBatch")
    @ApiOperation(value = "批量删除字典数据", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除字典数据")
    @OperationLogger(value = "批量删除字典数据")
    public ApiResult deleteBatch(@RequestBody List<Long> ids){
        return dictDataService.deleteBatch(ids);
    }

    @RequestMapping(value = "/getDataByDictType",method = RequestMethod.POST)
    public ApiResult getDataByDictType(@RequestBody List<String> types){
        return dictDataService.getDataByDictType(types);
    }
}

