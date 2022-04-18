package com.shiyi.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shiyi.annotation.OperationLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.utils.UploadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/file")
@Api(tags = "图片上传-接口")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadController {

    private final UploadUtil uploadUtil;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @SaCheckPermission("/file/upload")
    @ApiOperation(value = "上传图片",httpMethod = "POST", response = ResponseResult.class, notes = "上传图片")
    public ResponseResult upload(MultipartFile multipartFile){
        return uploadUtil.upload(multipartFile);
    }

    @RequestMapping(value = "/delBatchFile",method = RequestMethod.POST)
    @SaCheckPermission("/file/delBatchFile")
    @ApiOperation(value = "批量删除文件",httpMethod = "POST", response = ResponseResult.class, notes = "批量删除文件")
    @OperationLogger("批量删除图片")
    public ResponseResult delBatchFile(String key){
        return uploadUtil.delBatchFile(key)? ResponseResult.success("删除成功") : ResponseResult.error("删除失败");
    }
}
