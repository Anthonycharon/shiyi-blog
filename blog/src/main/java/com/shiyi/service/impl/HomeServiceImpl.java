package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.common.*;
import com.shiyi.dto.CategoryCountDTO;
import com.shiyi.dto.HomeDataDTO;
import com.shiyi.entity.*;
import com.shiyi.enums.PublishEnum;
import com.shiyi.mapper.*;
import com.shiyi.service.SystemConfigService;
import com.shiyi.service.WebConfigService;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.IpUtils;
import com.shiyi.utils.RedisCache;
import com.shiyi.dto.ContributeDTO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private TagsMapper tagsMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserLogMapper sysLogMapper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private WebConfigService webConfigService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 文章、留言、用户、ip统计
     * @return
     */
    public Map<String,Integer> lineCount(){
        Map<String,Integer> map = new HashMap<>();
        map.put("article", articleMapper.selectList(null).size());
        map.put("message",messageMapper.selectList(null).size());
        map.put("user",userMapper.selectCount(null));
        map.put("ipCount",sysLogMapper.getToday());
        return map;
    }

    public HomeDataDTO init() {
        //文章排行
        List<BlogArticle> blogArticles = articleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
                .select(BlogArticle::getQuantity,BlogArticle::getTitle,BlogArticle::getId)
                .eq(BlogArticle::getIsPublish, PublishEnum.PUBLISH.getCode())
                .orderByDesc(BlogArticle::getQuantity).last("limit 5"));
        //文章贡献度
        Map<String, Object> contribute = this.contribute();
        //标签统计
        Map<String, Object> categoryCount = this.categoryCount();
        //用户访问量
        List<Map<String, Object>> userAccess = this.userAccess();
        //弹出框
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        HomeDataDTO dto = HomeDataDTO.builder().dashboard(systemConfig.getDashboardNotification())
                .categoryList(categoryCount).contribute(contribute).blogArticles(blogArticles).userAccess(userAccess).build();
        return dto;
    }

    //----------web端开始------------
    /**
     * 获取站点信息
     * @return
     */
    public ApiResult webSiteInfo() {
        //网站信息
        WebConfig webConfig = webConfigService.getOne(new LambdaQueryWrapper<WebConfig>()
                .select(WebConfig::getAuthorAvatar,WebConfig::getIsMusicPlayer,WebConfig::getAuthorInfo,WebConfig::getTouristAvatar,WebConfig::getBulletin,
                        WebConfig::getQqNumber,WebConfig::getGitee,WebConfig::getGithub,WebConfig::getLogo,
                        WebConfig::getAboutMe,WebConfig::getEmail,WebConfig::getShowList,WebConfig::getLoginTypeList,
                        WebConfig::getRecordNum,WebConfig::getAuthor,WebConfig::getAliPay,WebConfig::getWeixinPay,
                        WebConfig::getWebUrl, WebConfig::getSummary,WebConfig::getName,WebConfig::getKeyword)
        .last(SysConf.LIMIT_ONE));

        //文章分类标签
        Integer articleCount = articleMapper.selectCount(new QueryWrapper<BlogArticle>().eq(SqlConf.IS_PUBLISH, PublishEnum.PUBLISH.getCode()));
        Integer tagCount = tagsMapper.selectCount(null);
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询访问量
        Object count = redisCache.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT);
        String viewsCount = Optional.ofNullable(count).orElse(0).toString();
        Map<String,Object> map = new HashMap<>();
        map.put("articleCount",articleCount);
        map.put("categoryCount",categoryCount);
        map.put("tagCount",tagCount);
        map.put("viewsCount",viewsCount);

        //查询页面信息
        List<Page> pages = pageMapper.selectList(new LambdaQueryWrapper<Page>()
                .select(Page::getPageCover,Page::getPageLabel));


        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageList",pages);
        resultMap.put("webSite",webConfig);
        resultMap.put("count",map);
        return ApiResult.success(resultMap);

    }

    /**
     * 添加访问量
     * @param request
     * @return
     */
    public ApiResult report(HttpServletRequest request) {
        // 获取ip
        String ipAddress = IpUtils.getIp(request);
        // 获取访问设备
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisCache.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getCityInfo(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(Constants.PROVINCE, "")
                        .replaceAll(Constants.CITY, "");
                redisCache.hIncr(RedisConstants.VISITOR_AREA, ipSource, 1L);
            } else {
                redisCache.hIncr(RedisConstants.VISITOR_AREA, Constants.UNKNOWN, 1L);
            }
            // 访问量+1
            redisCache.incr(RedisConstants.BLOG_VIEWS_COUNT, 1);
            // 保存唯一标识
            redisCache.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        return ApiResult.ok();
    }

    //--------------自定义方法开始---------------
    public static List<String> getMonths() {
        List<String> dateList = new ArrayList<String>();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        dateList.add(DateUtils.formateDate(calendar.getTime(),DateUtils.YYYY_MM_DD));
        while (date.after(calendar.getTime())) { //倒序时间,顺序after改before其他相应的改动。
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(DateUtils.formateDate(calendar.getTime(),DateUtils.YYYY_MM_DD));
        }
        return dateList;
    }
    /**
     * 获取文章贡献度
     * @return
     */
    public Map<String, Object> contribute() {
        Map<String, Object> map = new HashMap<>();
        List<Object> contributes = new ArrayList<>();
        List<Object> result = new ArrayList<>();
        List<String> months = getMonths();
        String lastTime = months.get(0), nowTime = months.get(months.size() - 1);
        List<ContributeDTO> articles = articleMapper.contribute(lastTime, nowTime);
        months.forEach(item -> {
            List<Object> list = new ArrayList<>();
            list.add(item);
            List<ContributeDTO> collect = articles.stream().filter(article -> article.getDate().equals(item)).collect(Collectors.toList());
            if (!collect.isEmpty()) list.add(collect.get(0).getCount());
            else list.add(0);
            result.add(list);
        });
        contributes.add(lastTime);
        contributes.add(nowTime);
        map.put("contributeDate", contributes);
        map.put("blogContributeCount", result);
        return map;
    }

    /**
     * 分类统计
     * @return
     */
    public Map<String, Object> categoryCount(){
        Map<String, Object> map = new HashMap<>();
        List<CategoryCountDTO> result = categoryMapper.countArticle();
        List<String> list = new ArrayList<>();
        result.forEach(item -> list.add(item.getName()));
        map.put("result",result);
        map.put("categoryList",list);
        return map;
    }

    /**
     * 获取用户访问数据
     * @return
     */
    public List<Map<String,Object>> userAccess() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        List<Map<String,Object>> userAccess = sysLogMapper.getUserAccess(DateUtils.formateDate(calendar.getTime(),DateUtils.YYYY_MM_DD));
        return userAccess;
    }
}
