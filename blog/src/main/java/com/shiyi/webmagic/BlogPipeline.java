package com.shiyi.webmagic;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.common.SqlConf;
import com.shiyi.common.SysConf;
import com.shiyi.entity.BlogArticle;
import com.shiyi.entity.Tags;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.RandomUtil;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.text.MessageFormat;
import java.util.*;

import static com.shiyi.common.ResultCode.CRAWLING_ARTICLE_FAILED;

/**
 * @author blue
 * @date 2021/12/22
 * @apiNote
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogPipeline implements Pipeline {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPipeline.class);

    private final ArticleMapper articleMapper;

    private final TagsMapper tagsMapper;

    private final RestTemplate restTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Object title = resultItems.get("title");
        List<Object> tagList = resultItems.get("tags");
        Object content = resultItems.get("content");
        Object originalUrl = resultItems.get("url");
        Assert.isTrue(StringUtils.isNotBlank(content.toString()),CRAWLING_ARTICLE_FAILED.getDesc());

        //爬取的是HTML内容，需要转成MD格式的内容
        String newContent = content.toString().replaceAll("<code>", "<code class=\"lang-java\">");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)
                .replace("lang-java","java");

        //文章封面图片 由https://picsum.photos该网站随机获取
        String url = MessageFormat.format("https://picsum.photos/id/{0}/info", RandomUtil.generationOneNumber(1000));
        Map map = restTemplate.getForObject(url, Map.class);

        BlogArticle entity = BlogArticle.builder().userId(SysConf.ADMIN_USER_ID).contentMd(markdown)
                .categoryId(SysConf.OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode()).originalUrl(originalUrl.toString())
                .title(title.toString()).avatar(map.get("download_url").toString()).content(newContent).build();

        articleMapper.insert(entity);
        //为该文章添加标签
        List<Long> tagsId = new ArrayList<>();
        tagList.forEach(item ->{
            Tags result = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(SqlConf.NAME, item.toString()));
            if (result == null){
                result = Tags.builder().name(item.toString()).build();
                tagsMapper.insert(result);
            }
            tagsId.add(result.getId());
        });
        tagsMapper.saveArticleToTags(entity.getId(),tagsId);

        LOGGER.info("文章抓取成功，内容为:{}", JSON.toJSONString(entity));
    }
}
