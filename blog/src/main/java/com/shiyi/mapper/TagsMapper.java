package com.shiyi.mapper;

import com.shiyi.entity.Tags;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 博客标签表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Repository
public interface TagsMapper extends BaseMapper<Tags> {

    void saveArticleToTags(@Param("articleId") Long articleId, @Param("tags")List<Long> tags);

    void deleteArticleToTags(@Param("ids") List<Long> ids);

    List<Long> findByArticleId(Long articleId);

    List<String> getTagsName(Long articleId);

    List<Long> findByTagId(Long tagId);

    List<Tags> findByArticleIdToTags(Long id);

    int countArticle(Long id);
}
