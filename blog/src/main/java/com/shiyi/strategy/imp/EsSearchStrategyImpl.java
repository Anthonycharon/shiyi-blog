package com.shiyi.strategy.imp;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shiyi.dto.ArticleSearchDTO;
import com.shiyi.common.Constants;
import com.shiyi.common.SqlConf;
import com.shiyi.strategy.SearchStrategy;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("elasticsearchStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EsSearchStrategyImpl implements SearchStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EsSearchStrategyImpl.class);

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        return search(buildQuery(keywords));
    }

    /**
     * 搜索文章构造
     *
     * @param keywords 关键字
     * @return es条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(String keywords) {
        // 条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 根据关键词搜索文章标题或内容
        boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery(SqlConf.TITLE, keywords))
                .should(QueryBuilders.matchQuery(SqlConf.CONTENT, keywords)));
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 文章搜索结果高亮
     *
     * @param nativeSearchQueryBuilder es条件构造器
     * @return 搜索结果
     */
    private List<ArticleSearchDTO> search(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 添加文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field(SqlConf.TITLE);
        titleField.preTags(Constants.PRE_TAG);
        titleField.postTags(Constants.POST_TAG);
        // 添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field(SqlConf.CONTENT);
        contentField.preTags(Constants.PRE_TAG);
        contentField.postTags(Constants.POST_TAG);
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        // 搜索
        try {
            SearchHits<ArticleSearchDTO> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ArticleSearchDTO.class);
            return search.getSearchHits().stream().map(hit -> {
                ArticleSearchDTO article = hit.getContent();
                // 获取文章标题高亮数据
                List<String> titleHighLightList = hit.getHighlightFields().get(SqlConf.TITLE);
                if (CollectionUtils.isNotEmpty(titleHighLightList)) {
                    // 替换标题数据
                    article.setTitle(titleHighLightList.get(0));
                }
                // 获取文章内容高亮数据
                List<String> contentHighLightList = hit.getHighlightFields().get(SqlConf.CONTENT);
                if (CollectionUtils.isNotEmpty(contentHighLightList)) {
                    // 替换内容数据
                    article.setContent(contentHighLightList.get(contentHighLightList.size() - 1));
                }
                return article;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
