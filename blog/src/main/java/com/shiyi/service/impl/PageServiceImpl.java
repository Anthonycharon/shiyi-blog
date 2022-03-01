package com.shiyi.service.impl;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Page;
import com.shiyi.mapper.PageMapper;
import com.shiyi.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-26
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    /**
     * 页面列表
     * @return
     */
    @Override
    public ApiResult listData() {
        List<Page> pages = baseMapper.selectList(null);
        return ApiResult.success(pages);
    }

    /**
     * 添加页面
     * @param page
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addPage(Page page) {
        int rows = baseMapper.insert(page);
        return rows > 0 ? ApiResult.success(page):ApiResult.fail("添加失败");
    }

    /**
     * 修改页面
     * @param page
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePage(Page page) {
        int rows = baseMapper.updateById(page);
        return rows > 0 ? ApiResult.ok():ApiResult.fail("添加失败");
    }

    /**
     * 删除页面
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deletePage(Long id) {
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? ApiResult.ok():ApiResult.fail("添加失败");
    }
}
