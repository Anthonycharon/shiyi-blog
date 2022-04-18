package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system/comment")
@Api(tags = "评论管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentController {

    private final CommentService commentService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "评论列表", httpMethod = "GET", response = ApiResult.class, notes = "评论列表")
    public ApiResult list(String keywords){
        return commentService.listData(keywords);
    }

    @RequestMapping(value = "/deleteBatch",method = RequestMethod.DELETE)
    @SaCheckPermission("/system/comment/deleteBatch")
    @ApiOperation(value = "批量删除评论", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除评论")
    @OperationLogger(value = "删除评论")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids){
        return commentService.deleteBatch(ids);
    }

}