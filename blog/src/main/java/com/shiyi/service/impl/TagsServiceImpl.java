package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.SysConf;
import com.shiyi.entity.BlogArticle;
import com.shiyi.entity.Tags;
import com.shiyi.common.ApiResult;
import com.shiyi.common.RedisConstants;
import com.shiyi.common.SqlConf;
import com.shiyi.enums.PublishEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.TagsMapper;
import com.shiyi.service.TagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 博客标签表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-09-09
 */
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements TagsService {

    /**
     * 标签列表
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(String name, Integer pageNo, Integer pageSize) {
        QueryWrapper<Tags> queryWrapper = new QueryWrapper<Tags>()
                .like(StringUtils.isNotBlank(name),SqlConf.NAME,name).orderByDesc(SqlConf.SORT);
        Page<Tags> list = baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        list.getRecords().forEach(item ->{
            int count = baseMapper.countArticle(item.getId());
            item.setArticleCount(count);
        });
        return ApiResult.success(list);
    }

    /**
     * 标签详情
     * @param id
     * @return
     */
    @Override
    public ApiResult info(Long id) {
        Tags tags = baseMapper.selectById(id);
        return ApiResult.success(tags);
    }

    /**
     * 添加标签
     * @param tags
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addTag(Tags tags) {
        validateName(tags.getName());
        baseMapper.insert(tags);
        return ApiResult.ok();
    }

    /**
     * 修改标签
     * @param tags
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateTag(Tags tags) {
        Tags entity = baseMapper.selectById(tags.getId());
        if (!entity.getName().equals(tags.getName())) validateName(tags.getName());
        baseMapper.updateById(tags);
        return ApiResult.ok();
    }

    /**
     * 删除标签
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(Long id) {
        baseMapper.deleteById(id);
        return ApiResult.ok();
    }

    /**
     * 批量删除标签
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ApiResult.ok();
    }

    /**
     * 置顶标签
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult top(Long id) {
        Tags tags = baseMapper.selectOne(new QueryWrapper<Tags>()
                .last(SysConf.LIMIT_ONE).orderByDesc(SqlConf.SORT));
        Assert.isTrue(!tags.getId().equals(id),"改标签已在最顶端!");
        Tags entity = Tags.builder().id(id).sort(tags.getSort()+1).build();
        int rows = baseMapper.updateById(entity);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("操作失败");
    }


    //    ----web端方法开始-----
    /**
     *  标签列表
     * @return
     */
    @Override
    public ApiResult webList() {
        List<Tags> list = baseMapper.selectList(new LambdaQueryWrapper<Tags>()
        .select(Tags::getId,Tags::getName).orderByDesc(Tags::getSort));
        return ApiResult.success(list);
    }

    //-----------自定义方法开始------------
    public void validateName(String name){
        Tags entity = baseMapper.selectOne(new QueryWrapper<Tags>().eq(SqlConf.NAME,name));
        Assert.isNull(entity,"标签名已存在!");
    }
}
