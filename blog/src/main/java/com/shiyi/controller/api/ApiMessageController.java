package com.shiyi.controller.api;


import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Message;
import com.shiyi.service.MessageService;
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
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/web/message")
@Api(tags = "评论留言接口")
public class ApiMessageController {

    @Autowired
    private MessageService messageService;

    @BusinessLog(value = "留言模块-留言列表",type = "查询",desc = "留言列表")
    @RequestMapping(value = "/webMessage",method = RequestMethod.GET)
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = ApiResult.class, notes = "留言列表")
    public ApiResult webMessage(){
        return messageService.webMessage();
    }


    @BusinessLog(value = "留言模块-用户留言",type = "添加",desc = "用户留言")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加留言", httpMethod = "POST", response = ApiResult.class, notes = "添加留言")
    public ApiResult addMessage(@RequestBody Message message){
        return messageService.webAddMessage(message);
    }

}

