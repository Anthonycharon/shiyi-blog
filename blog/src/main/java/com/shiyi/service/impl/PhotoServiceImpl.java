package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Photo;
import com.shiyi.mapper.PhotoMapper;
import com.shiyi.service.PhotoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Autowired
    private PhotoMapper mapper;

    /**
     * 照片列表
     * @param albumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(Integer albumId, Integer pageNo, Integer pageSize) {
        Page<Photo> photoPage = baseMapper.selectPage(new Page<>(pageNo, pageSize), new QueryWrapper<Photo>().eq(SqlConf.ALBUM_ID, albumId));
        return ApiResult.success(photoPage);
    }

    /**
     * 照片详情
     * @param id
     * @return
     */
    @Override
    public ApiResult infoPhoto(Integer id) {
        Photo photo = baseMapper.selectById(id);
        return ApiResult.success(photo);
    }

    /**
     * 添加照片
     * @param photo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addPhoto(Photo photo) {
        int rows = baseMapper.insert(photo);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("添加照片失败");
    }

    /**
     * 修改照片
     * @param photo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePhoto(Photo photo) {
        int rows = baseMapper.updateById(photo);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("修改照片失败");
    }

    /**
     * 移动照片
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult movePhoto(Map<String,Object> map) {
        Integer albumId = (Integer) map.get("albumId");
        List<Integer> ids = (List<Integer>) map.get("ids");
        mapper.movePhoto(ids,albumId);
        return ApiResult.ok();
    }

    /**
     * 批量删除照片
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("删除照片失败");
    }
}
