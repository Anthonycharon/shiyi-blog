package com.shiyi.controller.system;

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
    @ApiOperation(value = "文章列表", httpMethod = "POST", response = ApiResult.class, notes = "文章列表")
    public ApiResult list(@RequestBody Map<String,Object> map) {
        return articleService.listData(map);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ApiResult.class, notes = "文章详情")
    public ApiResult info(Long id) {
        return articleService.info(id);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "保存文章", httpMethod = "POST", response = ApiResult.class, notes = "保存文章")
    @OperationLogger(value = "保存文章")
    public ApiResult save( @RequestBody ArticleVO article) {
        return  articleService.addArticle(article);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改文章", httpMethod = "POST", response = ApiResult.class, notes = "修改文章")
    @OperationLogger(value = "修改文章")
    public ApiResult update(@RequestBody ArticleVO article) {
        return articleService.updateArticle(article);
    }

    @PostMapping(value = "/pubOrShelf")
    @ApiOperation(value = "发布或下架文章", httpMethod = "POST", response = ApiResult.class, notes = "发布或下架文章")
    @OperationLogger(value = "发布或下架文章")
    public ApiResult pubOrShelf(@RequestBody ArticleVO article) {
        return articleService.pubOrShelf(article);
    }

    @DeleteMapping(value = "/delete")
    @ApiOperation(value = "删除文章", httpMethod = "DELETE", response = ApiResult.class, notes = "删除文章")
    @OperationLogger(value = "删除文章")
    public ApiResult delete(Long id) {
        return articleService.removeAll(id);
    }

    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除文章", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除文章")
    @OperationLogger(value = "批量删除文章")
    public ApiResult deleteBatch(@RequestBody List<Long> ids) {
        return articleService.deleteBatch(ids);
    }

    @PostMapping(value = "/baiduSeo")
    @ApiOperation(value = "文章SEO", httpMethod = "POST", response = ApiResult.class, notes = "文章SEO")
    @OperationLogger(value = "文章SEO")
    public ApiResult baiduSeo(@RequestBody List<Long> ids) {
        return articleService.baiduSeo(ids);
    }

    @GetMapping(value = "/reptile")
    @ApiOperation(value = "文章爬虫", httpMethod = "GET", response = ApiResult.class, notes = "文章爬虫")
    @OperationLogger(value = "文章爬虫")
    public ApiResult reptile(String url) {
        return articleService.reptile(url);
    }

}
