package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.PhotoAlbum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    ApiResult listData(String name);

    ApiResult infoAlbum(Integer id);

    ApiResult addAlbum(PhotoAlbum photoAlbum);

    ApiResult updateAlbum(PhotoAlbum photoAlbum);

    ApiResult deleteAlbum(Integer id);





    //web端方法开始
    ApiResult webAlbumList();

    ApiResult webListPhotos(Integer albumId);

}
