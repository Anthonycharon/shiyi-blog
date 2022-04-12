package com.shiyi.controller.api;


import com.shiyi.common.ApiResult;
import com.shiyi.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@RestController
@RequestMapping("/web/album")
@Api(tags = "相册接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiPhotoAlbumController {

    private final PhotoAlbumService albumService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ApiResult.class, notes = "相册列表")
    public ApiResult webAlbumList(){
        return albumService.webAlbumList();
    }

    @RequestMapping(value = "/listPhotos",method = RequestMethod.GET)
    @ApiOperation(value = "照片列表", httpMethod = "GET", response = ApiResult.class, notes = "照片列表")
    public ApiResult webListPhotos(Integer albumId){
        return albumService.webListPhotos(albumId);
    }
}

