package com.shiyi.controller.index;


import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class ApiPhotoAlbumController {

    @Autowired
    private PhotoAlbumService albumService;

    @BusinessLog(value = "相册模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ApiResult.class, notes = "相册列表")
    @IgnoreUrl
    public ApiResult query(){
        return albumService.webList();
    }

    @BusinessLog(value = "相册模块-用户访问页面",type = "查询",desc = "用户访问页面")
    @RequestMapping(value = "/listPhotos",method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ApiResult.class, notes = "相册列表")
    @IgnoreUrl
    public ApiResult webListPhotos(Integer albumId,Integer pageNo,@RequestParam(defaultValue = "10") Integer pageSize){
        return albumService.webListPhotos(albumId,pageNo,pageSize);
    }
}

