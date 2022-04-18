package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.BlogArticle;
import com.shiyi.entity.Category;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.CategoryMapper;
import com.shiyi.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.shiyi.common.ResultCode.CATEGORY_IS_EXIST;
import static com.shiyi.common.ResultCode.CATEGORY_IS_TOP;
import static com.shiyi.common.SqlConf.LIMIT_ONE;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    /**
     * 分类列表
     * @param name
     * @return
     */
    @Override
    public ApiResult listData(String name) {
        Page<Category> categoryPage = baseMapper.selectPageRecord(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), name);
        return ApiResult.success(categoryPage);
    }

    /**
     * 分类详情
     * @param id
     * @return
     */
    @Override
    public ApiResult infoCategory(Long id) {
        Category category = baseMapper.selectById(id);
        return ApiResult.success(category);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteCategory(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("删除分类失败");
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(SqlConf.NAME, category.getName()));
        Assert.isNull(vo,"该分类名称已存在!");
        int rows = baseMapper.insert(category);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("添加分类失败");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(SqlConf.NAME, category.getName()));
        Assert.isTrue(!(vo != null && !vo.getId().equals(category.getId())),CATEGORY_IS_EXIST.getDesc());

        int rows = baseMapper.updateById(category);

        return rows > 0 ?ApiResult.ok():ApiResult.fail("修改分类失败");
    }

    /**
     * 置顶分类
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult top(Long id) {
        Category category = baseMapper.selectOne(new QueryWrapper<Category>().orderByDesc(SqlConf.SORT).last(LIMIT_ONE));
        Assert.isTrue(!category.getId().equals(id), CATEGORY_IS_TOP.getDesc());

        Category vo = Category.builder()
                .sort(category.getSort() + 1).updateTime(DateUtils.getNowDate()).id(id).build();
        int rows = baseMapper.updateById(vo);

        return rows > 0?ApiResult.ok():ApiResult.fail("置顶失败");
    }

    /**
     * 批量删除分类
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Category> list) {
        List<Long> ids = new ArrayList<>();
        list.forEach(item -> ids.add(item.getId()));

        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除分类失败");
    }


    //-----------------web端方法开始-------------

    /**
     * 分类列表
     * @return
     */
    @Override
    public ApiResult webList() {
        List<Category> categories = baseMapper.selectList(new LambdaQueryWrapper<Category>().select(Category::getId, Category::getName)
        .orderByDesc(Category::getSort));
        categories.forEach(item ->{
            Integer count = articleMapper.selectCount(new QueryWrapper<BlogArticle>().eq(SqlConf.CATEGORY_ID, item.getId()));
            item.setArticleCount(count);
        });
        return ApiResult.success(categories);
    }
}
