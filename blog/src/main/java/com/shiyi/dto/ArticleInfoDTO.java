package com.shiyi.dto;


import com.shiyi.entity.Category;
import com.shiyi.entity.Comment;
import com.shiyi.entity.Tags;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ArticleInfoDTO {

    /**
     * 主键id
     */
    private Long id;
    /**
     *分类id
     */
    private Long categoryId;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面地址
     */
    private String avatar;
    /**
     * 内容
     */
    private String content;
    /**
     * 文章内容MD版
     */
    private String contentMd;
    /**
     * 是否私密
     */
    private Integer isSecret;
    /**
     * 是否原创
     */
    private Integer isOriginal;
    /**
     * 转发地址
     */
    private String originalUrl;
    /**
     * 阅读量
     */
    private Integer quantity;
    /**
     * SEO关键词
     */
    private String keywords;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 分类
     */
    private Category category;
    /**
     * 评论集合
     */
    private List<Comment> comments = new ArrayList<>();
    /**
     * 标签集合
     */
    private List<TagDTO> tagList = new ArrayList<>();
    /**
     * 最新文章
     */
    private List<LatestArticleDTO> newestArticleList = new ArrayList<>();
    /**
     * 上一篇
     */
    private LatestArticleDTO lastArticle;
    /**
     * 下一篇
     */
    private LatestArticleDTO nextArticle;
    /**
     * 推荐
     */
    private List<LatestArticleDTO> recommendArticleList = new ArrayList<>();
    /**
     * 点赞量
     */
    private Integer likeCount;

}
