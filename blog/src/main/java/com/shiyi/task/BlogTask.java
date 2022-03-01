package com.shiyi.task;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shiyi.entity.BlogArticle;
import com.shiyi.entity.Tags;
import com.shiyi.service.ArticleService;
import com.shiyi.common.RedisConstants;
import com.shiyi.service.TagsService;
import com.shiyi.utils.QiNiuUtil;
import com.shiyi.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author blue
 * @date 2021/12/8
 * @apiNote 定时任务调度测试
 */
@Component("blogTask")
@Slf4j
public class BlogTask {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Autowired
    QiNiuUtil qiNiuUtil;
    @Autowired
    private TagsService tagsService;


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
        long time = System.currentTimeMillis();
        // 获取带阅读量的前缀key集合
        Collection<String> keys = redisCache.keys(RedisConstants.READING_PREFIX+"*");
        List<BlogArticle> blogArticles = new ArrayList<>();
        // 取出所有数据更新到数据库
        for (String key : keys) {
            Long id = Long.parseLong(key.split(RedisConstants.READING_PREFIX)[1]);
            Integer number = redisCache.getCacheObject(key);
            BlogArticle blogArticle = new BlogArticle();
            blogArticle.setId(id);
            blogArticle.setQuantity(number);
            blogArticles.add(blogArticle);
        }
        articleService.updateBatchById(blogArticles);
        log.info("自动更新阅读数结束,用时:{}ms",(System.currentTimeMillis() - time));
    }

    /**
     * 删除七牛云的垃圾图片
     *
     */
    public void removeQiNiu(){
        log.info("定时清理七牛云图片开始------");
        long time = System.currentTimeMillis();
        Set<String> imgs = redisCache.diff(RedisConstants.ALL_IMG, RedisConstants.APPLY_IMG);
        String[] keys = imgs.toArray(new String[0]);
        qiNiuUtil.delBatchFile(keys);
        log.info("定时清理七牛云图片结束,用时:{}ms",(System.currentTimeMillis() - time));
    }

    /**
     * 删除redis当天验证码通过的ip
     *
     */
    public void removeCode(){
        log.info("定时清理redis验证通过IP开始------");
        long time = System.currentTimeMillis();
        redisCache.deleteObject(RedisConstants.CHECK_CODE_IP);
        log.info("定时清理redis验证通过IP结束,用时:{}ms",(System.currentTimeMillis() - time));
    }

    /**
     * 每天定时修改标签的点击量
     *
     */
    public void autoTagsClickVolume(){
        log.info("定时修改标签的点击量开始------" + new Date());
        Collection<String> keys = redisCache.keys(RedisConstants.TAGS_PREFIX+"*");
        List<Tags> tagsList = new ArrayList<>();
        // 取出所有数据更新到数据库
        for (String key : keys) {
            String StringId = key.split(RedisConstants.TAGS_PREFIX)[1];
            Long id = Long.parseLong(StringId);
            Integer number = redisCache.getCacheObject(key);
            Tags tags = new Tags();
            tags.setId(id);
            tags.setClickVolume(number);
            tagsList.add(tags);
        }
        tagsService.updateBatchById(tagsList);
        log.info("定时修改标签的点击量结束------" + new Date());
    }
}
