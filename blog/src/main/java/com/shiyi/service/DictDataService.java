package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface DictDataService extends IService<DictData> {

    ApiResult listDictData(Integer dictId, Integer isPublish);

    ApiResult addDictData(DictData dictData);

    ApiResult updateDictData(DictData dictData);

    ApiResult deleteBatch(List<Long> ids);

    ApiResult delete(Long id);

    ApiResult getDataByDictType(List<String> types);

}
