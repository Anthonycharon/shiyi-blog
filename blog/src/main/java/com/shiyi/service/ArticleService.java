package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.dto.ArticleDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface ArticleService extends IService<BlogArticle> {

    /**
     * 后台分页获取文章
     * @param map 参数map
     * @return
     */
    ResponseResult selectArticle(Map<String,Object> map);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    ResponseResult info(Long id);

    /**
     * 添加文章
     * @param article 文章对象
     * @return
     */
    ResponseResult insertArticle(ArticleDTO article);

    /**
     * 修改文章
     * @param article 文章对象
     * @return
     */
    ResponseResult updateArticle(ArticleDTO article);

    /**
     * 后台根据文章id删除文章
     * @param id 文章id
     * @return
     */
    ResponseResult deleteById(Long id);

    /**
     * 后台批量删除文章
     * @param ids 文章id集合
     * @return
     */
    ResponseResult deleteBatch(List<Long> ids);

    /**
     * 置顶文章
     * @param article 文章对象
     * @return
     */
    ResponseResult putTopArticle(ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     * @return
     */
    ResponseResult publishAndShelf(ArticleDTO article);

    /**
     * 百度seo
     * @param ids 文章id集合
     * @return
     */
    ResponseResult baiduSeo(List<Long> ids);

    /**
     * 爬取文章
     * @param url 文章地址
     * @return
     */
    ResponseResult reptile(String url);

    /**
     * 随机获取图片
     * @return
     */
    ResponseResult randomImg();




    //    ----------web端开始------

    /**
     * 首页分页获取文章
     * @return
     */
    ResponseResult webArticleList();

    /**
     * 首页获取文章详情
     * @param id 文章id
     * @return
     */
    ResponseResult webArticleInfo(Integer id);

    /**
     * 根据分类id或标签id获取文章
     * @param categoryId 分类id
     * @param tagId 标签id
     * @param pageSize 每页数量
     * @return
     */
    ResponseResult condition(Long categoryId, Long tagId, Integer pageSize);

    /**
     * 校验秘钥
     * @param code 验证码
     * @return
     */
    ResponseResult checkSecret(String code);

    /**
     * 文章归档
     * @return
     */
    ResponseResult archive();

    /**
     * 搜索文章
     * @param keywords 搜索关键词
     * @return
     */
    ResponseResult searchArticle(String keywords);

    /**
     * 文章点赞
     * @param articleId 文章id
     * @return
     */
    ResponseResult articleLike(Integer articleId);
}
