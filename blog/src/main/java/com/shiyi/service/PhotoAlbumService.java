package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult listData(String name);

    ResponseResult infoAlbum(Integer id);

    ResponseResult addAlbum(PhotoAlbum photoAlbum);

    ResponseResult updateAlbum(PhotoAlbum photoAlbum);

    ResponseResult deleteAlbum(Integer id);





    //web端方法开始
    ResponseResult webAlbumList();

    ResponseResult webListPhotos(Integer albumId);

}
