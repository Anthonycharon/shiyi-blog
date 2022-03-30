package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.service.DictDataService;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.common.SysConf;
import com.shiyi.entity.Dict;
import com.shiyi.entity.DictData;
import com.shiyi.enums.PublishEnum;
import com.shiyi.mapper.DictDataMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.service.DictService;
import com.shiyi.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典数据表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final DictService dictService;

    /**
     * 获取字典数据列表
     * @param dictId
     * @param isPublish
     * @return
     */
    @Override
    public ApiResult listDictData(Integer dictId, Integer isPublish) {
        QueryWrapper<DictData> queryWrapper = new QueryWrapper<DictData>()
                .eq(SqlConf.DICT_TYPE_ID,dictId).eq(isPublish != null,SqlConf.IS_PUBLISH,isPublish);
        Page<DictData> data = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        data.getRecords().forEach(item ->{
            Dict dict = dictService.getById(item.getDictId());
            item.setDict(dict);
        });
        return ApiResult.success(data);
    }

    /**
     * 添加字典数据
     * @param dictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addDictData(DictData dictData) {
        // 判断添加的字典数据是否存在
        isExist(dictData);
        baseMapper.insert(dictData);
        return ApiResult.ok();
    }

    /**
     * 修改字典数据
     * @param sysDictData
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateDictData(DictData sysDictData) {

        DictData dictData = baseMapper.selectOne(new QueryWrapper<DictData>().eq(SqlConf.DICT_LABEL,sysDictData.getLabel()));
        if (dictData != null && !dictData.getId().equals(sysDictData.getId())) return ApiResult.fail("该标签已存在!");

        baseMapper.updateById(sysDictData);
        return ApiResult.ok();
    }

    /**
     * 批量删除字典数据
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ApiResult.ok();
    }

    /**
     * 删除
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
     * 根据字典类型获取字典数据
     * @param types
     * @return
     */
    @Override
    public ApiResult getDataByDictType(List<String> types) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SqlConf.TYPE,types).eq(SqlConf.IS_PUBLISH, PublishEnum.PUBLISH.getCode());
        List<Dict> dictList = dictService.list(queryWrapper);
        dictList.forEach(item ->{
            QueryWrapper<DictData> sysDictDataQueryWrapper = new QueryWrapper<>();
            sysDictDataQueryWrapper.eq(SqlConf.IS_PUBLISH, PublishEnum.PUBLISH.getCode());
            sysDictDataQueryWrapper.eq(SqlConf.DICT_TYPE_ID, item.getId());
            sysDictDataQueryWrapper.orderByAsc(SqlConf.SORT);
            List<DictData> dataList = baseMapper.selectList(sysDictDataQueryWrapper);
            String defaultValue = null;
            for (DictData dictData : dataList) {
                //选取默认值
                if (dictData.getIsDefault().equals(SysConf.ONE)){
                    defaultValue = dictData.getValue();
                    break;

                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put(SysConf.DEFAULT_VALUE,defaultValue);
            result.put(SysConf.LIST,dataList);
            map.put(item.getType(),result);
        });
        return ApiResult.success(map);
    }

    //-------------自定义方法开始-----------
    public void isExist(DictData dictData){
        DictData temp = baseMapper.selectOne(new QueryWrapper<DictData>()
                .eq(SqlConf.DICT_LABEL, dictData.getLabel())
                .eq(SqlConf.DICT_TYPE_ID, dictData.getDictId())
                .last(SysConf.LIMIT_ONE));
        Assert.notNull(temp,"该数据标签已存在!");
    }
}
