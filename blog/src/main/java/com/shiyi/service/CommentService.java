package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Comment;
import com.shiyi.vo.CommentVO;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface CommentService extends IService<Comment> {

    ApiResult listData(String keywords);

    ApiResult deleteBatch(List<Integer> ids);



//    ------web端方法开始------
    ApiResult comments(Long articleId);

    ApiResult addComment(CommentVO comment);

    ApiResult repliesByComId(Integer commentId);

}
