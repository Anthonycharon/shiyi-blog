package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.vo.ArticleVO;

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

    ResponseResult listData(Map<String,Object> map);

    ResponseResult deleteById(Long id);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult info(Long id);

    ResponseResult addArticle(ArticleVO article);

    ResponseResult updateArticle(ArticleVO article);

    ResponseResult topArticle(ArticleVO article);

    ResponseResult baiduSeo(List<Long> ids);

    ResponseResult reptile(String url);

    ResponseResult pubOrShelf(ArticleVO article);

    ResponseResult randomImg();




    //    ----------web端开始------
    ResponseResult webArticleList();

    ResponseResult webArticleInfo(Integer id);

    ResponseResult condition(Long categoryId, Long tagId, Integer pageSize);

    ResponseResult checkSecret(String code);

    ResponseResult archive();

    ResponseResult searchArticle(String keywords);

    ResponseResult articleLike(Integer articleId);
}
