package com.shiyi.webmagic;

import com.shiyi.common.SqlConf;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author blue
 * @date 2021/12/22
 * @apiNote
 */
@Component
public class CSDNPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000);

    @Override
    public void process(Page page) {
        page.putField("title", page.getHtml().xpath("//h1[@class='title-article']/text()").toString());
        page.putField("tags", page.getHtml().xpath("//a[@class='tag-link']/text()").all());
        page.putField("content", page.getHtml().xpath("//div[@class='article_content clearfix']").toString());
        page.putField("url", page.getUrl());
        if (page.getResultItems().get(SqlConf.TITLE) == null) {
            // 如果是列表页，跳过此页，pipeline不进行后续处理
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
