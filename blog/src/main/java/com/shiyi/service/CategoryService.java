package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 博客分类表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface CategoryService extends IService<Category> {

    ApiResult listData(String name);

    ApiResult addCategory(Category category);

    ApiResult top(Long id);

    ApiResult deleteBatch(List<Category> list);

    ApiResult updateCategory(Category category);

    ApiResult infoCategory(Long id);

    ApiResult deleteCategory(Long id);



    //web端方法开始
    ApiResult webList();

}
