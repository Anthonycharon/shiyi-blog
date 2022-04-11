package com.shiyi.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.dto.CommentDTO;
import com.shiyi.dto.ReplyCountDTO;
import com.shiyi.dto.ReplyDTO;
import com.shiyi.dto.SystemCommentDTO;
import com.shiyi.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 博客评论表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentDTO> listComments(@Param("page") int page, @Param("size") int size, @Param("articleId") Long articleId);

    List<ReplyDTO> listReplies(Integer id);

    ReplyCountDTO listReplyCountByCommentId(Integer id);

    Page<SystemCommentDTO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);
}
