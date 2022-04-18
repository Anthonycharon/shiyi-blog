package com.shiyi.service;

import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
public interface DictService extends IService<Dict> {

    ResponseResult listData(String name, Integer isPublish, String descColumn, String ascColumn);

    ResponseResult addSysDict(Dict dict);

    ResponseResult updateSysDict(Dict dict);

    ResponseResult delete(int id);

    ResponseResult deleteBatch(List<Long> list);

}
