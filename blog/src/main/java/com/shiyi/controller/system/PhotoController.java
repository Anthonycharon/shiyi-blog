package com.shiyi.controller.system;


import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.Photo;
import com.shiyi.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/system/photo")
@Api(tags = "相册管理")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "照片列表", httpMethod = "GET", response = ApiResult.class, notes = "照片列表")
    public ApiResult query(Integer albumId, Integer pageNo, Integer pageSize) {
        return photoService.listData(albumId,pageNo,pageSize);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "照片详情", httpMethod = "GET", response = ApiResult.class, notes = "照片详情")
    public ApiResult infoAlbum(Integer id) {
        return photoService.infoPhoto(id);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "添加照片", httpMethod = "POST", response = ApiResult.class, notes = "添加照片")
    @OperationLogger(value = "添加照片")
    public ApiResult addAlbum(@RequestBody Photo photo) {
        return photoService.addPhoto(photo);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改照片", httpMethod = "POST", response = ApiResult.class, notes = "修改照片")
    @OperationLogger(value = "修改照片")
    public ApiResult updateAlbum(@RequestBody Photo photo) {
        return photoService.updatePhoto(photo);
    }

    @PostMapping(value = "/movePhoto")
    @ApiOperation(value = "移动照片", httpMethod = "POST", response = ApiResult.class, notes = "移动照片")
    @OperationLogger(value = "移动照片")
    public ApiResult movePhoto(@RequestBody Map<String,Object> map) {
        return photoService.movePhoto(map);
    }

    @DeleteMapping(value = "/deleteBatch")
    @ApiOperation(value = "批量删除照片", httpMethod = "DELETE", response = ApiResult.class, notes = "批量删除照片")
    @OperationLogger(value = "批量删除照片")
    public ApiResult deleteBatch(@RequestBody List<Integer> ids) {
        return photoService.deleteBatch(ids);
    }

}

