package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Photo;
import com.shiyi.entity.PhotoAlbum;
import com.shiyi.enums.YesOrNoEnum;
import com.shiyi.mapper.PhotoAlbumMapper;
import com.shiyi.mapper.PhotoMapper;
import com.shiyi.service.PhotoAlbumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.util.PageUtils;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {


    private final PhotoMapper photoMapper;

    /**
     * 相册列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult selectAlbum(String name) {
        QueryWrapper<PhotoAlbum> queryWrapper = new QueryWrapper<PhotoAlbum>()
                .like(StringUtils.isNotBlank(name),SqlConf.NAME,name);
        Page<PhotoAlbum> photoAlbumPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        photoAlbumPage.getRecords().forEach(item ->{
            Integer count = photoMapper.selectCount(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, item.getId()));
            item.setPhotoCount(count);
        });
        return ResponseResult.success(photoAlbumPage);
    }

    /**
     * 相册详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult infoAlbum(Integer id) {
        PhotoAlbum photoAlbum = baseMapper.selectById(id);
        Integer count = photoMapper.selectCount(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, photoAlbum.getId()));
        photoAlbum.setPhotoCount(count);
        return ResponseResult.success(photoAlbum);
    }

    /**
     * 添加相册
     * @param photoAlbum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertAlbum(PhotoAlbum photoAlbum) {
        int rows = baseMapper.insert(photoAlbum);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("添加相册失败");
    }

    /**
     * 修改相册
     * @param photoAlbum
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateAlbum(PhotoAlbum photoAlbum) {
        int rows = baseMapper.updateById(photoAlbum);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("修改相册失败");
    }

    /**
     * 删除相册
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteAlbum(Integer id) {
        baseMapper.deleteById(id);
        int rows = photoMapper.delete(new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, id));
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("删除相册失败");
    }


    //------------web端方法开始--------------

    /**
     * 相册列表
     * @return
     */
    @Override
    public ResponseResult webAlbumList() {
        List<PhotoAlbum> photoAlbums = baseMapper.selectList(new LambdaQueryWrapper<PhotoAlbum>().select(PhotoAlbum::getId, PhotoAlbum::getName,
                PhotoAlbum::getInfo, PhotoAlbum::getCover).eq(PhotoAlbum::getStatus, YesOrNoEnum.NO.getCode()));

        return ResponseResult.success(photoAlbums);
    }

    /**
     * 照片列表
     * @param albumId
     * @return
     */
    @Override
    public ResponseResult webListPhotos(Integer albumId) {
        Page<Photo> photoPage = photoMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), new LambdaQueryWrapper<Photo>().select(Photo::getUrl).eq(Photo::getAlbumId, albumId));
        List<String> urlList = new ArrayList<>();
        photoPage.getRecords().forEach(item -> urlList.add(item.getUrl()));

        PhotoAlbum photoAlbum = baseMapper.selectById(albumId);

        Map<String,Object> result = new HashMap<>();
        result.put("photoList",urlList);
        result.put("photoAlbumCover",photoAlbum.getCover());
        result.put("photoAlbumName",photoAlbum.getName());

        return ResponseResult.success(result);
    }
}
