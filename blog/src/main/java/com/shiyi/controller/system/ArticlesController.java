package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.service.ArticleService;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.vo.ArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system/article")
@Api(tags = "后台文章管理")
public class ArticlesController {

    @Autowired
    private ArticleService articleService;

    @PostMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "文章列表", httpMethod = "POST", response = ApiResult.class, notes = "文章列表")
    public ApiResult list(@RequestBody Map<String,Object> map) {
        return articleService.listData(map);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/article/info")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ApiResult.class, notes = "文章详情")
    public ApiResult info(Long id) {
        return articleService.info(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/article/add")
    @ApiOperation(value = "保存文章", httpMethod = "POST", response = ApiResult.class, notes = "保存文章")
    @OperationLogger(value = "保存文章")
    public ApiResult save( @RequestBody ArticleVO article) {
        return  articleService.addArticle(article);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/article/update")
    @ApiOperation(value = "修改文章", httpMethod = "POST", response = ApiResult.class, notes = "修改文章")
    @OperationLogger(value = "修改文章")
    public ApiResult update(@RequestBody ArticleVO article) {
        return articleService.updateArticle(article);
    }

    @PostMapping(value = "/pubOrShelf")
    @SaCheckPermission("/system/article/pubOrShelf")
    @ApiOperation(value = "发布或下架文章", httpMethod = "POST", response = ApiResult.class, notes = "发布或下架文章")
    @OperationLogger(value = "发布或下架文章")
    public ApiResult pubOrShelf(@RequestBody ArticleVO article) {
        return articleService.pubOrShelf(article);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/article/delete")
    @ApiOperation(value = "删除文章", httpMethod = "DELETE", response = ApiResult.class, notes = "删除文章")
    @OperationLogger(value = "删除文章")
    public ApiResult delete(Long id) {
        return articleService.removeAll(id);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/article/deleteBatch")
    @ApiOperation(value = "批量删除文章", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除文章")
    @OperationLogger(value = "批量删除文章")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) {
        return articleService.deleteBatch(ids);
    }

    @PostMapping(value = "/baiduSeo")
    @SaCheckPermission("/system/article/baiduSeo")
    @ApiOperation(value = "文章SEO", httpMethod = "POST", response = ApiResult.class, notes = "文章SEO")
    @OperationLogger(value = "文章SEO")
    public ApiResult baiduSeo(@RequestBody List<Long> ids) {
        return articleService.baiduSeo(ids);
    }

    @GetMapping(value = "/reptile")
    @SaCheckPermission("/system/article/reptile")
    @ApiOperation(value = "文章爬虫", httpMethod = "GET", response = ApiResult.class, notes = "文章爬虫")
    @OperationLogger(value = "文章爬虫")
    public ApiResult reptile(String url) {
        return articleService.reptile(url);
    }

    @GetMapping(value = "/randomImg")
    @ApiOperation(value = "随机获取一张图片", httpMethod = "GET", response = ApiResult.class, notes = "随机获取一张图片")
    public ApiResult randomImg() {
        return articleService.randomImg();
    }

}
