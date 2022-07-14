package com.shiyi.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.dto.*;
import com.shiyi.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface ArticleMapper extends BaseMapper<BlogArticle> {

    List<ContributeDTO> contribute(@Param("lastTime") String lastTime, @Param("nowTime")String nowTime);

    List<LatestArticleDTO> listRecommendArticles(@Param("articleId") Integer articleId);

    LatestArticleDTO getNextOrLastArticle(@Param("id") Integer id, @Param("type") Integer type, @Param("publish")int code);



    List<LatestArticleDTO> getNewArticles(@Param("id") Integer id, @Param("publish") int code);

    ArticleVO info(Long id);

    Page<ArticleListDTO> selectRecordPage(@Param("page") Page<Object> page, @Param("param") Map<String,Object> map);

    void pubOrShelf(@Param("article") ArticleVO article);

    ArticleInfoDTO selectPrimaryKeyById(@Param("id") Integer id);

    Page<ArticlePreviewDTO> selectPreviewPage(@Param("page") Page<Object> page, @Param("publish")int publish,
                                                  @Param("categoryId") Long categoryId,@Param("tagId") Long tagId);

    Page<ArticlePreviewDTO> selectArchivePage(@Param("page")Page<Object> page, @Param("code")int code);
}
