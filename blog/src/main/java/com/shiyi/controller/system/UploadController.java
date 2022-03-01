package com.shiyi.controller.system;

import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ApiResult;
import com.shiyi.utils.QiNiuUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/file")
@Api(tags = "图片上传-接口")
public class UploadController {

    @Autowired
    private QiNiuUtil qiNiuUtil;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ApiOperation(value = "上传图片",httpMethod = "POST", response = ApiResult.class, notes = "上传图片")
    @IgnoreUrl
    public ApiResult upload(MultipartFile multipartFile){
        return qiNiuUtil.upload(multipartFile);
    }

    @RequestMapping(value = "/delBatchFile",method = RequestMethod.POST)
    @ApiOperation(value = "批量删除文件",httpMethod = "POST", response = ApiResult.class, notes = "批量删除文件")
    @OperationLogger("批量删除图片")
    public ApiResult delBatchFile(String key){
        return qiNiuUtil.delBatchFile(key)?ApiResult.ok("删除成功"):ApiResult.ok("删除失败");
    }
}
