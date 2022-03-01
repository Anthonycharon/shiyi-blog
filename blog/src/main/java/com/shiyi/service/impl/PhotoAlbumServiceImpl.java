package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Photo;
import com.shiyi.entity.PhotoAlbum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.mapper.PhotoAlbumMapper;
import com.shiyi.mapper.PhotoMapper;
import com.shiyi.service.PhotoAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 相册 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {

    @Autowired
    private PhotoMapper photoMapper;

    /**
     * 相册列表
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(String name, Integer pageNo, Integer pageSize) {
        QueryWrapper<PhotoAlbum> queryWrapper = new QueryWrapper<PhotoAlbum>()
                .like(StringUtils.isNotBlank(name),SqlConf.NAME,name);
        Page<PhotoAlbum> photoAlbumPage = baseMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        photoAlbumPage.getRecords().forEach(item ->{
            Integer count = photoMapper.selectCount(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, item.getId()));
            item.setPhotoCount(count);
        });
        return ApiResult.success(photoAlbumPage);
    }

    /**
     * 相册详情
     * @param id
     * @return
     */
    @Override
    public ApiResult infoAlbum(Integer id) {
        PhotoAlbum photoAlbum = baseMapper.selectById(id);
        Integer count = photoMapper.selectCount(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, photoAlbum.getId()));
        photoAlbum.setPhotoCount(count);
        return ApiResult.success(photoAlbum);
    }

    /**
     * 添加相册
     * @param photoAlbum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addAlbum(PhotoAlbum photoAlbum) {
        int rows = baseMapper.insert(photoAlbum);
        return rows > 0 ? ApiResult.ok():ApiResult.fail("添加相册失败");
    }

    /**
     * 修改相册
     * @param photoAlbum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateAlbum(PhotoAlbum photoAlbum) {
        int rows = baseMapper.updateById(photoAlbum);
        return rows > 0 ? ApiResult.ok():ApiResult.fail("修改相册失败");
    }

    /**
     * 删除相册
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteAlbum(Integer id) {
        baseMapper.deleteById(id);
        int rows = photoMapper.delete(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, id));
        return rows > 0 ? ApiResult.ok():ApiResult.fail("删除相册失败");
    }


    //------------web端方法开始--------------

    /**
     * 相册列表
     * @return
     */
    @Override
    public ApiResult webList() {
        List<PhotoAlbum> photoAlbums = baseMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>().select(PhotoAlbum::getId, PhotoAlbum::getName,
                PhotoAlbum::getInfo, PhotoAlbum::getCover).eq(PhotoAlbum::getStatus, YesOrNoEnum.NO.getCode()));

        return ApiResult.success(photoAlbums);
    }

    /**
     * 照片列表
     * @param albumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult webListPhotos(Integer albumId, Integer pageNo, Integer pageSize) {
        Page<Photo> photoPage = photoMapper.selectPage(new Page<>(pageNo, pageSize), new LambdaQueryWrapper<Photo>().select(Photo::getUrl).eq(Photo::getAlbumId, albumId));
        List<String> urlList = new ArrayList<>();
        photoPage.getRecords().forEach(item -> urlList.add(item.getUrl()));

        PhotoAlbum photoAlbum = baseMapper.selectById(albumId);

        Map<String,Object> result = new HashMap<>();
        result.put("photoList",urlList);
        result.put("photoAlbumCover",photoAlbum.getCover());
        result.put("photoAlbumName",photoAlbum.getName());

        return ApiResult.success(result);
    }
}
