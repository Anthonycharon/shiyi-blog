package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult selectPage();

    ResponseResult insertPage(Page page);

    ResponseResult updatePage(Page page);

    ResponseResult deletePage(Long id);
}
