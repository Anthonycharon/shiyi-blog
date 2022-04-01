package com.shiyi.utils;

import com.alibaba.fastjson.JSON;
import com.shiyi.dto.ArticleSearchDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Component;

/**
 * @author blue
 * @date 2022/1/19
 * @apiNote Elasticsearch工具类
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ElasticsearchUtils {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchUtils.class);

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    /**
     * 新增数据
     * @param articleSearchDTO 数据对象
     */
    public void save(ArticleSearchDTO articleSearchDTO) {
        long time = System.currentTimeMillis();
        elasticsearchRestTemplate.save(articleSearchDTO);
        logger.info("耗时:"+(System.currentTimeMillis() - time));
    }

    /**
     * 删除数据
     * @param id 文章ID
     */
    public void delete(Long id) {
        elasticsearchRestTemplate.delete(id.toString(), ArticleSearchDTO.class);
    }

    /**
     * 修改数据
     * @param articleSearchDTO 数据对象
     */
    public void update(ArticleSearchDTO articleSearchDTO) {
        String obj = JSON.toJSONString(articleSearchDTO);
        Document document = Document.parse(obj);

        UpdateQuery query = UpdateQuery
                .builder(String.valueOf(articleSearchDTO.getId()))
                .withDocument(document)
                .build();

        IndexCoordinates indexCoordinates = elasticsearchRestTemplate.getIndexCoordinatesFor(ArticleSearchDTO.class);

        UpdateResponse update = elasticsearchRestTemplate.update(query, indexCoordinates);
    }
}
