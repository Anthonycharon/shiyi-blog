package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface PhotoService extends IService<Photo> {

    ApiResult listData(Integer albumId, Integer pageNo, Integer pageSize);

    ApiResult infoPhoto(Integer id);

    ApiResult addPhoto(Photo photo);

    ApiResult updatePhoto(Photo photo);

    ApiResult movePhoto(Map<String,Object> map);

    ApiResult deleteBatch(List<Integer> ids);

}
