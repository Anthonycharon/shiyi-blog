package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-09-26
 */
@RestController
@RequestMapping("/system/message")
@Api(tags = "留言管理-接口")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value="/list",method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = ApiResult.class, notes = "留言列表")
    public ApiResult listPage(String name,Integer pageNo, Integer pageSize){
        return messageService.listData(name,pageNo,pageSize);
    }

    @RequestMapping(value="/passBatch",method = RequestMethod.POST)
    @SaCheckPermission("/system/message/passBatch")
    @OperationLogger(value = "批量通过")
    @ApiOperation(value = "批量通过", httpMethod = "POST", response = ApiResult.class, notes = "批量通过")
    public ApiResult passBatch(@RequestBody List<Integer> ids){
        return messageService.passBatch(ids);
    }


    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/message/remove")
    @OperationLogger(value = "删除留言")
    @ApiOperation(value = "删除留言", httpMethod = "DELETE", response = ApiResult.class, notes = "删除留言")
    public ApiResult remove(int id){
        return messageService.deleteById(id);
    }


    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/message/deleteBatch")
    @OperationLogger(value = "批量删除留言")
    @ApiOperation(value = "批量删除留言", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除留言")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids){
        return messageService.deleteBatch(ids);
    }
}

