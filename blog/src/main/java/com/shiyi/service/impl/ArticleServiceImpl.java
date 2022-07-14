package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.dto.*;
import com.shiyi.common.*;
import com.shiyi.entity.*;
import com.shiyi.enums.SearchModelEnum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.mapper.*;
import com.shiyi.service.ArticleService;
import com.shiyi.service.SystemConfigService;
import com.shiyi.strategy.context.SearchStrategyContext;
import com.shiyi.utils.*;
import com.shiyi.vo.ArticleVO;
import com.shiyi.webmagic.BlogPipeline;
import com.shiyi.webmagic.CSDNPageProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Spider;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.shiyi.common.Constants.*;
import static com.shiyi.common.RedisConstants.*;
import static com.shiyi.common.ResultCode.*;
import static com.shiyi.enums.PublishEnum.PUBLISH;

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

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${baidu.url}")
    private String baiduUrl;


    /**
     *  后台获取所有文章
     * @return
     */
    @Override
    public ResponseResult listData(Map<String,Object> map) {
        Page<ArticleListDTO> data = baseMapper.selectRecordPage(new Page<>((Integer)map.get("pageNo"), (Integer)map.get("pageSize")),map);
        return ResponseResult.success(data);
    }

    /**
     *  后台获取文章详情
     * @return
     */
    @Override
    public ResponseResult info(Long id) {
        ArticleVO articleVO = baseMapper.info(id);
        articleVO.setTags(tagsMapper.getTagsName(id));
        return ResponseResult.success(articleVO);
    }

    /**
     *  添加文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addArticle(ArticleVO article) {
        BlogArticle blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        baseMapper.insert(blogArticle);
        tagsMapper.saveArticleToTags(blogArticle.getId(),tagList);
        return ResponseResult.success();
    }

    /**
     *  修改文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateArticle(ArticleVO article) {
        BlogArticle blogArticle = baseMapper.selectById(article.getId());
        Assert.notNull(blogArticle,ARTICLE_NOT_EXIST.getDesc());

        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        blogArticle.setCategoryId(categoryId);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        tagsMapper.deleteArticleToTags(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        tagsMapper.saveArticleToTags(blogArticle.getId(),tagList);

        return ResponseResult.success();
    }

    /**
     *  置顶文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult topArticle(ArticleVO article) {
        BlogArticle blogArticle = BeanCopyUtils.copyObject(article, BlogArticle.class);
        baseMapper.updateById(blogArticle);
        return ResponseResult.success();
    }


    /**
     *  文章百度推送
     * @return
     */
    @Override
    public ResponseResult baiduSeo(List<Long> ids) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.shiyit.com/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });
        return ResponseResult.success();
    }

    /**
     *  抓取文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult reptile(String url) {
        Spider  spider = Spider.create(new CSDNPageProcessor()).addUrl(url);
        spider.addPipeline(blogPipeline).thread(5).run();
        return ResponseResult.success();
    }

    /**
     *  发布或下架文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult pubOrShelf(ArticleVO article) {
        baseMapper.pubOrShelf(article);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult randomImg() {
        //文章封面图片 由https://api.btstu.cn/该网站随机获取
        String result = restTemplate.getForObject(IMG_URL_API, String.class);
        Object imgUrl = JSON.parseObject(result).get("imgurl");
        return ResponseResult.success(imgUrl);
    }

    /**
     *  删除文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteById(Long id) {
        baseMapper.deleteById(id);
        this.deleteAfter(Collections.singletonList(id));
        return ResponseResult.success("删除文章成功");
    }

    /**
     * 批量删除文章
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        this.deleteAfter(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("批量删除文章失败");
    }

    //    ----------web端方法开始-------
    /**
     *  获取文章列表
     * @return
     */
    @Override
    public ResponseResult webArticleList() {
        Page<ArticlePreviewDTO> articlePreviewDTOPage = baseMapper.selectPreviewPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), PUBLISH.code,null,null);
        articlePreviewDTOPage.getRecords().forEach(item -> item.setTagDTOList(tagsMapper.findByArticleIdToTags(item.getId())));
        return ResponseResult.success(articlePreviewDTOPage);
    }

    /**
     *  获取文章详情
     * @return
     */
    @Override
    public ResponseResult webArticleInfo(Integer id) {
        ArticleInfoDTO blogArticle = baseMapper.selectPrimaryKeyById(id);
        //标签
        List<TagDTO> tags = tagsMapper.findByArticleIdToTags(blogArticle.getId());
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
        List<LatestArticleDTO> blogArticles = baseMapper.getNewArticles(id, PUBLISH.getCode());
        blogArticle.setNewestArticleList(blogArticles);

        // 查询上一篇下一篇文章
        LatestArticleDTO lastArticle = baseMapper.getNextOrLastArticle(id,0, PUBLISH.getCode());
        blogArticle.setLastArticle(lastArticle);
        LatestArticleDTO nextArticle = baseMapper.getNextOrLastArticle(id,1, PUBLISH.getCode());
        blogArticle.setNextArticle(nextArticle);

        //相关推荐
        List<LatestArticleDTO> recommendArticleList = baseMapper.listRecommendArticles(id);
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
        threadPoolTaskExecutor.execute(() -> this.incr(id.longValue(),ARTICLE_READING));

        return ResponseResult.success(blogArticle);
    }

    /**
     *  获取分类or标签文章
     * @return
     */
    @Override
    public ResponseResult condition(Long categoryId, Long tagId, Integer pageSize) {
        Map<String,Object> result = new HashMap<>();
        Page<ArticlePreviewDTO>  blogArticlePage = baseMapper.selectPreviewPage(new Page<>(PageUtils.getPageNo(),pageSize),PUBLISH.getCode(),categoryId,tagId);
        blogArticlePage.getRecords().forEach(item -> {
            List<TagDTO> tagList = tagsMapper.findByArticleIdToTags(item.getId());
            item.setTagDTOList(tagList);
        });
        String name;
        if (categoryId != null){
            name = categoryMapper.selectById(categoryId).getName();
        }else {
            name = tagsMapper.selectById(tagId).getName();

            threadPoolTaskExecutor.execute(() ->this.incr(tagId,TAG_CLICK_VOLUME));
        }
        result.put(SqlConf.NAME,name);
        result.put(RECORDS,blogArticlePage.getRecords());
        return ResponseResult.success(result);
    }

    /**
     *  获取归档
     * @return
     */
    @Override
    public ResponseResult archive() {
        Page<ArticlePreviewDTO> articlePage = baseMapper.selectArchivePage(new Page<>(PageUtils.getPageNo(),PageUtils.getPageSize()),PUBLISH.code);
        return ResponseResult.success(articlePage);
    }

    /**
     *  搜索文章
     * @return
     */
    @Override
    public ResponseResult searchArticle(String keywords) {
        Assert.isTrue(StringUtils.isNotBlank(keywords), PARAMS_ILLEGAL.getDesc());
        //获取搜索模式（es搜索或mysql搜索）
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
        //搜索逻辑
        List<ArticleSearchDTO> articleSearchDTOS = searchStrategyContext.executeSearchStrategy(strategy, keywords);
        return ResponseResult.success(articleSearchDTOS);
    }

    /**
     * 文章点赞
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult articleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = ARTICLE_USER_LIKE + StpUtil.getLoginId();
        if (redisCache.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisCache.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisCache.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisCache.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisCache.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
        return ResponseResult.success();
    }

    /**
     *  校验文章验证码(验证码通过关注公众号获取)
     * @return
     */
    @Override
    public ResponseResult checkSecret(String code) {
        //校验验证码
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisCache.getCacheObject(key);
        Assert.isTrue(redisCode != null, ERROR_EXCEPTION_MOBILE_CODE.getDesc());

        //将ip存在redis 有效期一天，当天无需再进行验证码校验
        List<Object> cacheList = redisCache.getCacheList(CHECK_CODE_IP);
        if (cacheList.isEmpty()) {
            cacheList = new ArrayList<>();
        }
        cacheList.add(IpUtils.getIp(request));
        redisCache.setCacheList(CHECK_CODE_IP,cacheList);
        //通过删除验证码
        redisCache.deleteObject(key);
        return ResponseResult.success("验证成功");
    }


    //    -----自定义方法开始-------
    /**
     *  增加文字阅读量或标签点击量
     * @return
     */
    public void incr(Long id,String key) {
        Map<String, Object> map = redisCache.getCacheMap(key);
        Integer value = (Integer) map.get(id.toString());
        // 如果key存在就直接加一
        if (value != null) {
            map.put(id.toString(),value+1);
        }else {
            map.put(id.toString(),1);
        }
        redisCache.setCacheMap(key,map);
    }

    /**
     * 删除文章后的一些同步删除
     * @param ids
     */
    private void deleteAfter(List<Long> ids){
        tagsMapper.deleteArticleToTags(ids);
        threadPoolTaskExecutor.execute(()->this.deleteEsData(ids));
    }

    /**
     * 删除es索引库的数据
     * @param ids
     */
    private void deleteEsData(List<Long> ids){
        ids.forEach(elasticsearchUtils::delete);
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
