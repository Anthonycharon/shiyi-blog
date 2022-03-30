package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.entity.Tags;
import com.shiyi.common.ApiResult;

import java.util.List;

/**
 * <p>
 * 博客标签表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
public interface TagsService extends IService<Tags> {

    ApiResult listData(String name);

    ApiResult addTag(Tags tags);

    ApiResult updateTag(Tags tags);

    ApiResult delete(Long id);

    ApiResult deleteBatch(List<Long> ids);

    ApiResult info(Long id);

    ApiResult top(Long id);


    //    -----web端方法开始-----
    ApiResult webList();

}
