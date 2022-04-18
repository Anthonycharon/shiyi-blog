package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult listData(Integer albumId);

    ResponseResult infoPhoto(Integer id);

    ResponseResult addPhoto(Photo photo);

    ResponseResult updatePhoto(Photo photo);

    ResponseResult movePhoto(Map<String,Object> map);

    ResponseResult deleteBatch(List<Integer> ids);

}
