package com.shiyi.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.PhotoAlbum;
import com.shiyi.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/system/album")
@Api(tags = "相册管理")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhotoAlbumController {

    private final PhotoAlbumService albumService;


    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ApiResult.class, notes = "相册列表")
    public ApiResult query(String name,Integer pageNo, Integer pageSize) {
        return albumService.listData(name,pageNo,pageSize);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/album/info")
    @ApiOperation(value = "相册详情", httpMethod = "GET", response = ApiResult.class, notes = "相册详情")
    public ApiResult infoAlbum(Integer id) {
        return albumService.infoAlbum(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/album/add")
    @ApiOperation(value = "添加相册", httpMethod = "POST", response = ApiResult.class, notes = "添加相册")
    @OperationLogger(value = "添加相册")
    public ApiResult addAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return albumService.addAlbum(photoAlbum);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/album/update")
    @ApiOperation(value = "修改相册", httpMethod = "POST", response = ApiResult.class, notes = "修改相册")
    @OperationLogger(value = "修改相册")
    public ApiResult updateAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return albumService.updateAlbum(photoAlbum);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/album/delete")
    @ApiOperation(value = "删除相册", httpMethod = "DELETE", response = ApiResult.class, notes = "删除相册")
    @OperationLogger(value = "删除相册")
    public ApiResult deleteAlbum(Integer id) {
        return albumService.deleteAlbum(id);
    }
}

