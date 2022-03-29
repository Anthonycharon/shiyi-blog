package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.dto.ArticleListDTO;
import com.shiyi.dto.ArticleRecoDTO;
import com.shiyi.dto.ArticleSearchDTO;
import com.shiyi.common.*;
import com.shiyi.entity.*;
import com.shiyi.enums.SearchModelEnum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.*;
import com.shiyi.service.ArticleService;
import com.shiyi.enums.PublishEnum;
import com.shiyi.service.SystemConfigService;
import com.shiyi.strategy.context.SearchStrategyContext;
import com.shiyi.utils.*;
import com.shiyi.vo.ArticleVO;
import com.shiyi.webmagic.BlogPipeline;
import com.shiyi.webmagic.CSDNPageProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Spider;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, BlogArticle> implements ArticleService {

    private final CategoryMapper categoryMapper;

    private final SystemConfigService systemConfigService;

    private final RedisCache redisCache;

    private final TagsMapper tagsMapper;

    private final CommentMapper commentMapper;

    private final BlogPipeline blogPipeline;

    private final SearchStrategyContext searchStrategyContext;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private final ElasticsearchUtils elasticsearchUtils;

    @Value("${baidu.url}")
    private String baiduUrl;


    /**
     *  后台获取所有文章
     * @return
     */
    @Override
    public ApiResult listData(Map<String,Object> map) {
        Page<ArticleListDTO> data = baseMapper.selectRecordPage(new Page<>((Integer)map.get("pageNo"), (Integer)map.get("pageSize")),map);
        return ApiResult.success(data);
    }

    /**
     *  后台获取文章详情
     * @return
     */
    @Override
    public ApiResult info(Long id) {
        ArticleVO articleVO = baseMapper.info(id);
        articleVO.setTags(tagsMapper.getTagsName(id));
        return ApiResult.success(articleVO);
    }

    /**
     *  添加文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addArticle(ArticleVO article) {
        BlogArticle blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        baseMapper.insert(blogArticle);
        tagsMapper.saveArticleToTags(blogArticle.getId(),tagList);
        return ApiResult.ok();
    }

    /**
     *  修改文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateArticle(ArticleVO article) {
        BlogArticle blogArticle = baseMapper.selectById(article.getId());
        Assert.notNull(blogArticle,"数据库未存在该文章!");

        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        blogArticle.setCategoryId(categoryId);
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        tagsMapper.deleteArticleToTags(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        tagsMapper.saveArticleToTags(blogArticle.getId(),tagList);

        return ApiResult.ok();
    }


    /**
     *  文章百度推送
     * @return
     */
    @Override
    public ApiResult baiduSeo(List<Long> ids) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.isblog.com.cn/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });
        return ApiResult.ok();
    }

    /**
     *  抓取文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult reptile(String url) {
        Spider  spider = Spider.create(new CSDNPageProcessor()).addUrl(url);
        spider.addPipeline(blogPipeline).thread(5).run();
        return ApiResult.ok();
    }

    /**
     *  发布或下架文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult pubOrShelf(ArticleVO article) {
        baseMapper.pubOrShelf(article);
        return ApiResult.ok();
    }

    @Override
    public ApiResult randomImg() {
        //文章封面图片 由https://picsum.photos该网站随机获取
        String url = MessageFormat.format("https://picsum.photos/id/{0}/info", RandomUtil.generationOneNumber(1000));
        String imgUrl = restTemplate.getForObject(url, Map.class).get("download_url").toString();
        return ApiResult.success(imgUrl);
    }

    /**
     *  删除文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult removeAll(Long id) {
        baseMapper.deleteById(id);
        this.deleteAfter(Collections.singletonList(id));
        return ApiResult.success("删除文章成功");
    }

    /**
     * 批量删除文章
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        this.deleteAfter(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除文章失败");
    }

    //    ----------web端方法开始-------
    /**
     *  获取文章列表
     * @return
     */
    @Override
    public ApiResult webArticleList(Integer pageNo, Integer pageSize) {
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<BlogArticle>()
                .eq(SqlConf.IS_PUBLISH, PublishEnum.PUBLISH.getCode()).orderByDesc(SqlConf.IS_STICK,SqlConf.CREATE_TIME);

        Page<BlogArticle> page = baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);

        page.getRecords().forEach(item ->{
            Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId, Category::getName)
                    .eq(Category::getId, item.getCategoryId()));
            item.setCategory(category);
            List<Tags> tags = tagsMapper.findByArticleIdToTags(item.getId());
            item.setTagList(tags);
        });
        return ApiResult.success(page);
    }

    /**
     *  获取文章详情
     * @return
     */
    @Override
    public ApiResult webArticleInfo(Integer id) {
        BlogArticle blogArticle = baseMapper.selectById(id);
        //标签
        List<Tags> tags = tagsMapper.findByArticleIdToTags(blogArticle.getId());
        blogArticle.setTagList(tags);
        //分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId,Category::getName)
        .eq(Category::getId,blogArticle.getCategoryId()));
        blogArticle.setCategory(category);
        //评论
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<Comment>()
                .eq(SqlConf.ARTICLE_ID, blogArticle.getId()).orderByDesc(SqlConf.CREATE_TIME);
        List<Comment> list = commentMapper.selectList(queryWrapper);
        blogArticle.setComments(list);

        //最新文章
        List<ArticleRecoDTO> blogArticles = baseMapper.getNewArticles(id,PublishEnum.PUBLISH.getCode());
        blogArticle.setNewestArticleList(blogArticles);

        // 查询上一篇下一篇文章
        ArticleRecoDTO lastArticle = baseMapper.getNextOrLastArticle(id,0,PublishEnum.PUBLISH.getCode());
        blogArticle.setLastArticle(lastArticle);
        ArticleRecoDTO nextArticle = baseMapper.getNextOrLastArticle(id,1,PublishEnum.PUBLISH.getCode());
        blogArticle.setNextArticle(nextArticle);

        //相关推荐
        List<ArticleRecoDTO> recommendArticleList = baseMapper.listRecommendArticles(id);
        blogArticle.setRecommendArticleList(recommendArticleList);

        // 封装点赞量和浏览量
        blogArticle.setLikeCount((Integer) redisCache.hGet(RedisConstants.ARTICLE_LIKE_COUNT, blogArticle.getId().toString()));

        //校验私密文章是否已经进行过验证
        if(blogArticle.getIsSecret().equals(YesOrNoEnum.YES.getCode())){
            List<Object> cacheList = redisCache.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtils.getIp(request);
            if (cacheList.contains(ip)) blogArticle.setIsSecret(YesOrNoEnum.NO.getCode());
        }

        //增加文章阅读量
        new Thread(() -> this.increaseRead(id)).start();

        return ApiResult.success(blogArticle);
    }

    /**
     *  获取分类or标签文章
     * @return
     */
    @Override
    public ApiResult condition(Long categoryId, Long tagId, Integer pageNo, Integer pageSize) {
        Map<String,Object> result = new HashMap<>();
        Page<BlogArticle> blogArticlePage;
        String name;
        if (categoryId != null){
            //分类
            Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId,Category::getName)
                    .eq(Category::getId,categoryId));
            blogArticlePage = baseMapper.selectPage(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<BlogArticle>()
                    .select(BlogArticle::getId,BlogArticle::getCategoryId, BlogArticle::getAvatar, BlogArticle::getTitle, BlogArticle::getCreateTime)
                    .eq(BlogArticle::getIsPublish, PublishEnum.PUBLISH.getCode()).eq(BlogArticle::getCategoryId,categoryId)
                    .orderByDesc(BlogArticle::getIsStick,BlogArticle::getCreateTime));
            for (BlogArticle blogArticle : blogArticlePage.getRecords()) {
                //标签
                List<Long> tagsId = tagsMapper.findByArticleId(blogArticle.getId());
                List<Tags> tagsList = tagsMapper.selectList(new LambdaQueryWrapper<Tags>().select(Tags::getId, Tags::getName)
                        .in(Tags::getId, tagsId));
                blogArticle.setTagList(tagsList);
                blogArticle.setCategory(category);
            }
            name = category.getName();
        }else {
            Tags tags = tagsMapper.selectById(tagId);
            List<Long> articleId = tagsMapper.findByTagId(tagId);
            blogArticlePage = baseMapper.selectPage(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<BlogArticle>()
                    .select(BlogArticle::getId,BlogArticle::getCategoryId, BlogArticle::getAvatar, BlogArticle::getTitle, BlogArticle::getCreateTime)
                    .eq(BlogArticle::getIsPublish, PublishEnum.PUBLISH.getCode()).in(BlogArticle::getId,articleId)
                    .orderByDesc(BlogArticle::getIsStick,BlogArticle::getCreateTime));
            for (BlogArticle blogArticle : blogArticlePage.getRecords()) {
                //标签
                blogArticle.getTagList().add(tags);
                Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId,Category::getName)
                        .eq(Category::getId,blogArticle.getCategoryId()));
                blogArticle.setCategory(category);
            }
            name = tags.getName();
        }
        result.put(SqlConf.NAME,name);
        result.put(SysConf.CURRENTPAGE,blogArticlePage.getCurrent());
        result.put(SysConf.RECORDS,blogArticlePage.getRecords());
        return ApiResult.success(result);
    }

    /**
     *  获取归档
     * @return
     */
    @Override
    public ApiResult archive(Integer pageNo, Integer pageSize) {

        Page<BlogArticle> articlePage = baseMapper.selectPage(new Page<>(pageNo,pageSize), new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getId, BlogArticle::getTitle, BlogArticle::getCreateTime)
                .orderByDesc(BlogArticle::getIsStick,BlogArticle::getCreateTime)
                .eq(BlogArticle::getIsPublish, PublishEnum.PUBLISH.getCode()));

        Integer articleCount = baseMapper.selectCount(new QueryWrapper<BlogArticle>().eq(SqlConf.IS_PUBLISH, PublishEnum.PUBLISH.getCode()));

        Map<String,Object> result = new HashMap<>();
        result.put("articleCount",articleCount);
        result.put("recordList",articlePage.getRecords());
        return ApiResult.success(result);
    }

    /**
     *  搜索文章
     * @return
     */
    @Override
    public ApiResult searchArticle(String keywords) {
        Assert.isTrue(StringUtils.isNotBlank(keywords),"关键词不合法!!");
        //获取搜索模式（es搜索或mysql搜索）
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        //搜索逻辑
        List<ArticleSearchDTO> articleSearchDTOS = searchStrategyContext.executeSearchStrategy(strategy, keywords);
        return ApiResult.success(articleSearchDTOS);
    }

    /**
     * 文章点赞 TODO 待完善
     * @param articleId
     * @return
     */
    @Override
    public ApiResult articleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = RedisConstants.ARTICLE_USER_LIKE + 1;
        if (redisCache.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisCache.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisCache.hDecr(RedisConstants.ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisCache.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisCache.hIncr(RedisConstants.ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
        return ApiResult.ok();
    }

    /**
     *  校验文章验证码(验证码通过关注公众号获取)
     * @return
     */
    @Override
    public ApiResult checkSecret(String code) {
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisCache.getCacheObject(key);
        Assert.isTrue(redisCode != null,"验证码不存在或已失效!");
        //校验通过删除验证码
        redisCache.deleteObject(key);
        //将ip存在redis 有效期一天，当天无需再进行验证码校验
        key = RedisConstants.CHECK_CODE_IP;
        List<Object> cacheList = redisCache.getCacheList(key);
        if (cacheList.isEmpty()) cacheList = new ArrayList<>();
        cacheList.add(IpUtils.getIp(request));
        redisCache.setCacheList(key,cacheList);
        return ApiResult.ok("验证成功");
    }


    //    -----自定义方法开始-------
    /**
     *  增加文章阅读量
     * @return
     */
    public void increaseRead(Integer id) {
        String key = RedisConstants.READING_PREFIX + id;
        if (redisCache.hasKey(key)) {
            redisCache.incr(key);   // 如果key存在就直接加一
        } else {
            redisCache.setCacheObject(key, 1);
        }
    }

    /**
     * 删除文章后的一些同步删除
     * @param ids
     */
    private void deleteAfter(List<Long> ids){
        ids.forEach(item-> redisCache.deleteObject(RedisConstants.READING_PREFIX+item));
        tagsMapper.deleteArticleToTags(ids);
        new Thread(()->this.deleteEsData(ids)).start();
    }

    /**
     * 删除es索引库的数据
     * @param ids
     */
    private void deleteEsData(List<Long> ids){
        ids.forEach(id -> elasticsearchUtils.delete(id));
    }

    /**
     * 将数据库不存在的标签新增
     * @param article
     * @return
     */
    private List<Long> getTagsList(ArticleVO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item ->{
            Tags tags = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(SqlConf.NAME, item));
            if (tags == null){
                tags = Tags.builder().name(item).sort(0).build();
                tagsMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    /**
     * 如果分类不存在则新增
     * @param categoryName
     * @return
     */
    private Long savaCategory(String categoryName) {
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().eq(SqlConf.NAME, categoryName));
        if (category == null){
            category = Category.builder().name(categoryName).sort(0).build();
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}
