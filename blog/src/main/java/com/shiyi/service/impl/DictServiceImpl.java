package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.common.SysConf;
import com.shiyi.entity.Dict;
import com.shiyi.entity.DictData;
import com.shiyi.mapper.DictMapper;
import com.shiyi.service.DictDataService;
import com.shiyi.service.DictService;
import com.shiyi.utils.HumpLineUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    private final DictDataService dictDataService;

    /**
     * 字典列表
     * @param name
     * @param isPublish
     * @param descColumn
     * @param ascColumn
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(String name, Integer isPublish, String descColumn, String ascColumn, int pageNo, int pageSize) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>()
                .eq(isPublish != null,SqlConf.IS_PUBLISH,isPublish)
                .like(StringUtils.isNotBlank(name),SqlConf.NAME,name);
        if(StringUtils.isNotEmpty(ascColumn)) {
            // 将驼峰转换成下划线
            String column = HumpLineUtil.humpToLine2(ascColumn);
            queryWrapper.orderByAsc(StringUtils.isNotEmpty(ascColumn),column);
        }else if(StringUtils.isNotEmpty(descColumn)) {
            // 将驼峰转换成下划线
            String column = HumpLineUtil.humpToLine2(descColumn);
            queryWrapper.orderByDesc(column);
        } else {
            queryWrapper.orderByDesc(SqlConf.SORT, SqlConf.CREATE_TIME);
        }
        Page<Dict> page = new Page<>(pageNo,pageSize);
        Page<Dict> data = baseMapper.selectPage(page, queryWrapper);
        return ApiResult.success(data);
    }

    /**
     * 添加字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addSysDict(Dict dict) {
        validateType(dict.getType());
        baseMapper.insert(dict);
        return ApiResult.ok();
    }

    /**
     * 修改字典
     * @param dict
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateSysDict(Dict dict) {
        Dict temp = baseMapper.selectById(dict.getId());
        if (!temp.getType().equals(dict.getType())) validateType(dict.getType());
        baseMapper.updateById(dict);
        return ApiResult.ok();
    }

    /**
     * 删除字典
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(int id) {
        int count  = dictDataService.count(new QueryWrapper<DictData>().eq(SqlConf.DICT_TYPE_ID,id));
        Assert.isTrue(count==0,"该字典类型存在字典数据!");
        baseMapper.deleteById(id);
        return ApiResult.ok();
    }

    /**
     * 批量删除字典
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Long> ids) {
        int count  = dictDataService.count(new QueryWrapper<DictData>().in(SqlConf.DICT_TYPE_ID,ids));
        Assert.isTrue(count==0,"所选字典类型中存在字典数据!");
        baseMapper.deleteBatchIds(ids);
        return ApiResult.ok();
    }


    /* ---------自定义方法开始------------*/
    public void validateType(String type){
        Dict temp  = baseMapper.selectOne(new QueryWrapper<Dict>().eq(SqlConf.TYPE, type).last(SysConf.LIMIT_ONE));
        Assert.isNull(temp,"该字典类型已存在!");
    }
}
