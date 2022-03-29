package com.shiyi.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.vo.CommentVO;
import com.shiyi.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/comment")
@Api(tags = "评论接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiCommentController {

    private final CommentService commentService;

    @BusinessLog(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/addComment",method = RequestMethod.POST)
    @SaCheckLogin
    @ApiOperation(value = "添加评论", httpMethod = "POST", response = ApiResult.class, notes = "添加评论")
    public ApiResult addComment(@RequestBody CommentVO comment){
        return commentService.addComment(comment);
    }

    @RequestMapping(value = "/comments",method = RequestMethod.GET)
    @ApiOperation(value = "查询文章评论", httpMethod = "GET", response = ApiResult.class, notes = "查询文章评论")
    public ApiResult comments(Integer pageNo,Integer pageSize,Long articleId){
        return commentService.comments( pageNo, pageSize,articleId);
    }

    @RequestMapping(value = "/repliesByComId",method = RequestMethod.GET)
    @ApiOperation(value = "查询评论回复", httpMethod = "GET", response = ApiResult.class, notes = "查询文章评论")
    public ApiResult repliesByComId(Integer pageNo,Integer pageSize,Integer commentId){
        return commentService.repliesByComId( pageNo, pageSize,commentId);
    }
}
