package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Comment;
import com.shiyi.vo.CommentVO;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface CommentService extends IService<Comment> {





//    ------web端方法开始------
    ApiResult webComment(Comment comment);

    ApiResult comments(Long articleId);

    ApiResult addComment(CommentVO comment);

    ApiResult repliesByComId(Integer pageNo, Integer pageSize, Integer commentId);
}
