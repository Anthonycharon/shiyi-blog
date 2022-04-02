package com.shiyi.service;

import com.shiyi.common.ApiResult;
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

    ApiResult listData(Map<String,Object> map);

    ApiResult removeAll(Long id);

    ApiResult deleteBatch(List<Long> ids);

    ApiResult info(Long id);

    ApiResult addArticle(ArticleVO article);

    ApiResult updateArticle(ArticleVO article);

    ApiResult baiduSeo(List<Long> ids);

    ApiResult reptile(String url);

    ApiResult pubOrShelf(ArticleVO article);

    ApiResult randomImg();




    //    ----------web端开始------
    ApiResult webArticleList();

    ApiResult webArticleInfo(Integer id);

    ApiResult condition(Long categoryId, Long tagId,Integer pageSize);

    ApiResult checkSecret(String code);

    ApiResult archive();

    ApiResult searchArticle(String keywords);

    ApiResult articleLike(Integer articleId);
}
