package com.shiyi.strategy.context;

import com.shiyi.dto.ArticleSearchDTO;
import com.shiyi.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author blue
 * @date 2022/1/5
 * @apiNote 第三方登录策略上下文
 */
@Service
public class SearchStrategyContext {

    @Autowired
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List<ArticleSearchDTO>} 搜索文章
     */
    public List<ArticleSearchDTO> executeSearchStrategy(String searchMode,String keywords) {
        return searchStrategyMap.get(searchMode).searchArticle(keywords);
    }

}
