package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.FeedBack;
import com.shiyi.mapper.FeedBackMapper;
import com.shiyi.service.FeedBackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2022-01-13
 */
@Service
public class FeedBackServiceImpl extends ServiceImpl<FeedBackMapper, FeedBack> implements FeedBackService {

    /**
     * 反馈列表
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(Integer type, Integer pageNo, Integer pageSize) {
        QueryWrapper<FeedBack> queryWrapper = new QueryWrapper<FeedBack>()
                .orderByDesc(SqlConf.CREATE_TIME).eq(type != null,SqlConf.TYPE,type);
        Page<FeedBack> feedBackPage = baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return ApiResult.success(feedBackPage);
    }

    /**
     * 删除反馈
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("删除反馈失败");
    }

    /**
     * 添加反馈
     * @param feedBack
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addFeedback(FeedBack feedBack) {
        int rows = baseMapper.insert(feedBack);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("添加反馈失败");
    }
}
