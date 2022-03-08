package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.dto.ReplyDTO;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Comment;
import com.shiyi.entity.User;
import com.shiyi.entity.UserAuth;
import com.shiyi.vo.CommentVO;
import com.shiyi.mapper.CommentMapper;
import com.shiyi.mapper.UserAuthMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.CommentService;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.HTMLUtils;
import com.shiyi.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    UserAuthMapper userAuthMapper;

    @Override
    public ApiResult webComment(Comment comment) {
      /*  try {
            DesUtils des = new DesUtils("isblog");//自定义密钥
            String openId = des.decrypt(comment.getEncodeId());
            QqUserInfo userInfo = qqUserInfoService.getOne(new QueryWrapper<QqUserInfo>().eq(SqlConf.OPEN_ID, openId));
            comment.setUserId(userInfo.getId());
            comment.setCreateTime(new Date());
            baseMapper.insert(comment);
            return ApiResult.ok("评论成功",comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("评论失败");
        }*/
        return  null;
    }

    @Override
    public ApiResult comments(Integer pageNo,Integer pageSize,Long articleId) {
        // 查询文章评论量
        Integer commentCount = baseMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(articleId), Comment::getArticleId, articleId)
                .isNull(Objects.isNull(articleId), Comment::getArticleId)
                .isNull(Comment::getParentId));
        if (commentCount == 0) {
            return ApiResult.ok();
        }
        Page<Comment> pages = baseMapper.selectPage(new Page<>(pageNo, pageSize),
                new QueryWrapper<Comment>().eq(SqlConf.ARTICLE_ID, articleId).isNull("parent_id")
                        .orderByDesc(SqlConf.ID));
        // 分页查询评论集合
        List<Comment> comments = pages.getRecords();
        if (CollectionUtils.isEmpty(comments)) {
            return ApiResult.ok();
        }
        List<com.shiyi.dto.CommentDTO> commentDTOList = new ArrayList<>();
        List<ReplyDTO> replyDTOList;
        for (Comment comment : comments) {
            User user = userMapper.selectById(comment.getUserId());
            // 根据评论id集合查询回复数据
            replyDTOList = commentMapper.listReplies(comment.getId());
            com.shiyi.dto.CommentDTO dto = new com.shiyi.dto.CommentDTO();
            dto.setId(comment.getId());
            dto.setUserId(comment.getUserId());
            dto.setCommentContent(comment.getContent());
            dto.setCreateTime(comment.getCreateTime());
            //dto.setAvatar(user.getAvatar());
            //dto.setNickname(user.getNickName());
            dto.setReplyDTOList(replyDTOList);
            dto.setReplyCount(replyDTOList.size());
            commentDTOList.add(dto);

        }

        Map<String,Object> map =new HashMap<>();
        map.put("commentCount",commentCount);
        map.put("commentDTOList",commentDTOList);
        return ApiResult.success(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addComment(CommentVO commentVO) {
        // 过滤标签
        commentVO.setCommentContent(HTMLUtils.deleteTag(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(commentVO.getUserId())
                .replyUserId(commentVO.getReplyUserId())
                .articleId(commentVO.getArticleId())
                .content(commentVO.getCommentContent())
                .parentId(commentVO.getParentId()).createTime(DateUtils.getNowDate())
                .build();
        int rows = baseMapper.insert(comment);
       /* // 判断是否开启邮箱通知,通知用户
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            notice(comment);
        }*/
        return rows > 0?ApiResult.success(comment):ApiResult.fail("评论失败");
    }

    @Override
    public ApiResult repliesByComId(Integer pageNo, Integer pageSize, Integer commentId) {
        Page<Comment> page = baseMapper.selectPage(new Page<>(pageNo, pageSize), new QueryWrapper<Comment>().eq(SqlConf.PARENT_ID, commentId));
        List<ReplyDTO> result = new ArrayList<>();
        for (Comment comment: page.getRecords()) {
            UserAuth userAuth = userAuthMapper.selectById(comment.getUserId());
            UserAuth replyUser = userAuthMapper.selectById(comment.getReplyUserId());
            ReplyDTO dto = new ReplyDTO();
            dto.setId(comment.getId());
            dto.setAvatar(userAuth.getAvatar());
            dto.setNickname(userAuth.getNickname());
            dto.setContent(comment.getContent());
            dto.setCreateTime(comment.getCreateTime());
            dto.setParentId(commentId);
            dto.setReplyNickname(replyUser.getNickname());
            result.add(dto);
        }
        return ApiResult.success(result);
    }
}
