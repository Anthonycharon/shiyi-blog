package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.vo.*;
import com.shiyi.common.*;
import com.shiyi.entity.*;
import com.shiyi.enums.SearchModelEnum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.*;
import com.shiyi.service.ArticleService;
import com.shiyi.service.SystemConfigService;
import com.shiyi.strategy.context.SearchStrategyContext;
import com.shiyi.utils.*;
import com.shiyi.dto.ArticleDTO;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, BlogArticle> implements ArticleService {

    private final CategoryMapper categoryMapper;

    private final SystemConfigService systemConfigService;

    private final RedisCache redisCache;

    private final TagsMapper tagsMapper;

    private final CommentMapper commentMapper;

    private final SearchStrategyContext searchStrategyContext;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private final ElasticsearchUtil elasticsearchUtil;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${baidu.url}")
    private String baiduUrl;


    /**
     *  后台获取所有文章
     * @return
     */
    @Override
    public ResponseResult selectArticle(Map<String,Object> map) {
        Page<ArticleListVO> data = baseMapper.selectArticle(new Page<>((Integer)map.get("pageNo"), (Integer)map.get("pageSize")),map);
        return ResponseResult.success(data);
    }

    /**
     *  后台获取文章详情
     * @return
     */
    @Override
    public ResponseResult info(Long id) {
        ArticleDTO articleDTO = baseMapper.selectPrimaryKey(id);
        articleDTO.setTags(tagsMapper.selectByArticleId(id));
        return ResponseResult.success(articleDTO);
    }

    /**
     *  添加文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertArticle(ArticleDTO article) {
        BlogArticle blogArticle = BeanCopyUtil.copyObject(article, BlogArticle.class);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        int insert = baseMapper.insert(blogArticle);
        if (insert > 0) {
            tagsMapper.saveArticleTags(blogArticle.getId(),tagList);
        }
        return ResponseResult.success();
    }

    /**
     *  修改文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateArticle(ArticleDTO article) {
        BlogArticle blogArticle = baseMapper.selectById(article.getId());
        Assert.notNull(blogArticle,ARTICLE_NOT_EXIST.getDesc());

        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtil.copyObject(article, BlogArticle.class);
        blogArticle.setCategoryId(categoryId);
        blogArticle.setUserId(StpUtil.getLoginIdAsLong());
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        tagsMapper.deleteByArticleIds(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        tagsMapper.saveArticleTags(blogArticle.getId(),tagList);

        return ResponseResult.success();
    }

    /**
     *  删除文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteById(Long id) {
        int row = baseMapper.deleteById(id);
        if (row > 0) {
            this.deleteAfter(Collections.singletonList(id));
        }
        return ResponseResult.success();
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
        if (rows > 0) {
            this.deleteAfter(ids);
        }
        return ResponseResult.success();
    }

    /**
     *  置顶文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult putTopArticle(ArticleDTO article) {
        baseMapper.putTopArticle(article);
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
        try {
            Document document = Jsoup.connect(url).get();
            Elements title  = document.getElementsByClass("title-article");
            Elements tags  = document.getElementsByClass("tag-link");
            Elements content  = document.getElementsByClass("article_content");
            Assert.isTrue(StringUtils.isNotBlank(content.toString()),CRAWLING_ARTICLE_FAILED.getDesc());

            //爬取的是HTML内容，需要转成MD格式的内容
            String newContent = content.get(0).toString().replaceAll("<code>", "<code class=\"lang-java\">");
            MutableDataSet options = new MutableDataSet();
            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)
                    .replace("lang-java","java");

            //文章封面图片 由https://api.btstu.cn/该网站随机获取
            String strResult = restTemplate.getForObject(IMG_URL_API, String.class);
            JSONObject jsonObject = JSON.parseObject(strResult);
            Object imgUrl = jsonObject.get("imgurl");

            BlogArticle entity = BlogArticle.builder().userId(7L).contentMd(markdown)
                    .categoryId(OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode()).originalUrl(url)
                    .title(title.get(0).text()).avatar(imgUrl.toString()).content(newContent).build();

            baseMapper.insert(entity);
            //为该文章添加标签
            List<Long> tagsId = new ArrayList<>();
            tags.forEach(item ->{
                String tag = item.text();
                Tags result = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(SqlConf.NAME,tag ));
                if (result == null){
                    result = Tags.builder().name(tag).build();
                    tagsMapper.insert(result);
                }
                tagsId.add(result.getId());
            });
            tagsMapper.saveArticleTags(entity.getId(),tagsId);

            log.info("文章抓取成功，内容为:{}", JSON.toJSONString(entity));
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return ResponseResult.success();
    }

    /**
     *  发布或下架文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publishAndShelf(ArticleDTO article) {
        baseMapper.publishAndShelf(article);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult randomImg() {
        //文章封面图片 由https://api.btstu.cn/该网站随机获取
        String result = restTemplate.getForObject(IMG_URL_API, String.class);
        Object imgUrl = JSON.parseObject(result).get("imgurl");
        return ResponseResult.success(imgUrl);
    }

    //    ----------web端方法开始-------
    /**
     *  获取文章列表
     * @return
     */
    @Override
    public ResponseResult webArticleList() {
        Page<ArticlePreviewVO> articlePreviewDTOPage = baseMapper.selectPreviewPage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()), PUBLISH.code,null,null);
        articlePreviewDTOPage.getRecords().forEach(item -> item.setTagVOList(tagsMapper.findByArticleIdToTags(item.getId())));
        return ResponseResult.success(articlePreviewDTOPage);
    }

    /**
     *  获取文章详情
     * @return
     */
    @Override
    public ResponseResult webArticleInfo(Integer id) {
        ArticleInfoVO blogArticle = baseMapper.selectPrimaryKeyById(id);
        //标签
        List<TagVO> tags = tagsMapper.findByArticleIdToTags(blogArticle.getId());
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
        List<LatestArticleVO> blogArticles = baseMapper.getNewArticles(id, PUBLISH.code);
        blogArticle.setNewestArticleList(blogArticles);

        // 查询上一篇下一篇文章
        LatestArticleVO lastArticle = baseMapper.getNextOrLastArticle(id,0, PUBLISH.code);
        blogArticle.setLastArticle(lastArticle);
        LatestArticleVO nextArticle = baseMapper.getNextOrLastArticle(id,1, PUBLISH.code);
        blogArticle.setNextArticle(nextArticle);

        //相关推荐
        List<LatestArticleVO> recommendArticleList = baseMapper.listRecommendArticles(id);
        blogArticle.setRecommendArticleList(recommendArticleList);

        // 封装点赞量和浏览量
        blogArticle.setLikeCount((Integer) redisCache.hGet(RedisConstants.ARTICLE_LIKE_COUNT, blogArticle.getId().toString()));

        //校验私密文章是否已经进行过验证
        if(blogArticle.getIsSecret().equals(YesOrNoEnum.YES.getCode())){
            List<Object> cacheList = redisCache.getCacheList(RedisConstants.CHECK_CODE_IP);
            String ip = IpUtil.getIp(request);
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
        Page<ArticlePreviewVO>  blogArticlePage = baseMapper.selectPreviewPage(new Page<>(PageUtil.getPageNo(),pageSize),PUBLISH.getCode(),categoryId,tagId);
        blogArticlePage.getRecords().forEach(item -> {
            List<TagVO> tagList = tagsMapper.findByArticleIdToTags(item.getId());
            item.setTagVOList(tagList);
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
        Page<ArticlePreviewVO> articlePage = baseMapper.selectArchivePage(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),PUBLISH.code);
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
        List<ArticleSearchVO> articleSearchVOS = searchStrategyContext.executeSearchStrategy(strategy, keywords);
        return ResponseResult.success(articleSearchVOS);
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
        cacheList.add(IpUtil.getIp(request));
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
        tagsMapper.deleteByArticleIds(ids);
        threadPoolTaskExecutor.execute(()->this.deleteEsData(ids));
    }

    /**
     * 删除es索引库的数据
     * @param ids
     */
    private void deleteEsData(List<Long> ids){
        ids.forEach(elasticsearchUtil::delete);
    }

    /**
     * 将数据库不存在的标签新增
     * @param article
     * @return
     */
    private List<Long> getTagsList(ArticleDTO article) {
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
