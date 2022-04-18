package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FriendLink;
import com.shiyi.service.FriendLinkService;
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
 * 友情链接表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/system/friend")
@Api(tags = "友情链接后端-接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "友链列表", httpMethod = "GET", response = ResponseResult.class, notes = "友链列表")
    public ResponseResult query(String name, Integer status){
        return friendLinkService.listData(name,status);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @SaCheckPermission("/system/friend/create")
    @ApiOperation(value = "添加友链", httpMethod = "POST", response = ResponseResult.class, notes = "添加友链")
    @OperationLogger(value = "添加友链")
    public ResponseResult create(@RequestBody FriendLink friendLink){
        return friendLinkService.addData(friendLink);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SaCheckPermission("/system/friend/update")
    @ApiOperation(value = "修改友链", httpMethod = "POST", response = ResponseResult.class, notes = "修改友链")
    @OperationLogger(value = "修改友链")
    public ResponseResult update(@RequestBody FriendLink friendLink){
        return friendLinkService.updateData(friendLink);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/friend/remove")
    @ApiOperation(value = "删除友链", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除友链")
    @OperationLogger(value = "删除友链")
    public ResponseResult remove(@RequestBody List<Integer> ids){
        return friendLinkService.delete(ids);
    }

    @RequestMapping(value = "/top",method = RequestMethod.GET)
    @SaCheckPermission("/system/friend/top")
    @ApiOperation(value = "置顶友链", httpMethod = "GET", response = ResponseResult.class, notes = "置顶友链")
    @OperationLogger(value = "置顶友链")
    public ResponseResult top(Integer id){
        return friendLinkService.top(id);
    }
}

