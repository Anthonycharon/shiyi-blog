package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult listData(String name);

    ResponseResult addCategory(Category category);

    ResponseResult top(Long id);

    ResponseResult deleteBatch(List<Category> list);

    ResponseResult updateCategory(Category category);

    ResponseResult infoCategory(Long id);

    ResponseResult deleteCategory(Long id);



    //web端方法开始
    ResponseResult webList();

}
