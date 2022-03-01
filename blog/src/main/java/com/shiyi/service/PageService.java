package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-26
 */
public interface PageService extends IService<Page> {

    ApiResult listData();

    ApiResult addPage(Page page);

    ApiResult updatePage(Page page);

    ApiResult deletePage(Long id);
}
