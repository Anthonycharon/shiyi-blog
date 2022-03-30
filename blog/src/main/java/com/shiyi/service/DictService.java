package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
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

    ApiResult listData(String name, Integer isPublish,String descColumn,String ascColumn);

    ApiResult addSysDict(Dict dict);

    ApiResult updateSysDict(Dict dict);

    ApiResult delete(int id);

    ApiResult deleteBatch(List<Long> list);

}
