package com.shiyi.quartz;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shiyi.entity.BlogArticle;
import com.shiyi.entity.Tags;
import com.shiyi.service.ArticleService;
import com.shiyi.common.RedisConstants;
import com.shiyi.service.TagsService;
import com.shiyi.utils.UploadUtil;
import com.shiyi.utils.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.shiyi.common.RedisConstants.ARTICLE_READING;
import static com.shiyi.common.RedisConstants.TAG_CLICK_VOLUME;

/**
 * @author blue
 * @date 2021/12/8
 * @apiNote 定时任务调度测试
 */
@Component("blogQuartz")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogQuartz {

    private final RedisCache redisCache;

    private final ArticleService articleService;

    private final UploadUtil uploadUtil;

    private final TagsService tagsService;


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    /**
     *  自动更新阅读数，每天凌晨四点更新数据
     * @author blue
     * @date: 2021/8/18 17:58
     */
    public void updateReadQuantity(){
        log.info("自动更新阅读数开始------");
        long time = getCurrentTimeMillis();
        // 获取带阅读量的前缀key集合
        List<BlogArticle> blogArticles = new ArrayList<>();
        Map<String, Object> map = redisCache.getCacheMap(ARTICLE_READING);
        // 取出所有数据更新到数据库
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            Integer value = (Integer) stringEntry.getValue();
            BlogArticle blogArticle = new BlogArticle(Long.parseLong(id),value);
            blogArticles.add(blogArticle);
        }
        articleService.updateBatchById(blogArticles);
        log.info("自动更新阅读数结束,用时:{}ms",(getCurrentTimeMillis() - time));
    }

    /**
     * 删除七牛云的垃圾图片
     *
     */
    public void removeQiNiu(){
        log.info("定时清理七牛云图片开始------");
        long time = getCurrentTimeMillis();
        Set<String> imgArrays = redisCache.diff(RedisConstants.ALL_IMG, RedisConstants.APPLY_IMG);
        String[] keys = imgArrays.toArray(new String[0]);
        uploadUtil.delBatchFile(keys);
        log.info("定时清理七牛云图片结束,用时:{}ms",(getCurrentTimeMillis() - time));
    }

    /**
     * 删除redis当天验证码通过的ip
     *
     */
    public void removeCode(){
        log.info("定时清理redis验证通过IP开始------");
        long time = getCurrentTimeMillis();
        redisCache.deleteObject(RedisConstants.CHECK_CODE_IP);
        log.info("定时清理redis验证通过IP结束,用时:{}ms",(getCurrentTimeMillis() - time));
    }

    /**
     * 每天定时修改标签的点击量
     *
     */
    public void autoTagsClickVolume(){
        log.info("定时修改标签的点击量开始------" + new Date());
        long time = getCurrentTimeMillis();
        Map<String, Object> map = redisCache.getCacheMap(TAG_CLICK_VOLUME);
        List<Tags> tagsList = new ArrayList<>();
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            Integer value = (Integer) stringEntry.getValue();
            Tags tags = new Tags(Long.parseLong(id),value);
            tagsList.add(tags);
        }
        tagsService.updateBatchById(tagsList);
        log.info("定时修改标签的点击量结束,用时:{}ms",(getCurrentTimeMillis() - time));
    }

    public long getCurrentTimeMillis (){
        return System.currentTimeMillis();
    }

}
