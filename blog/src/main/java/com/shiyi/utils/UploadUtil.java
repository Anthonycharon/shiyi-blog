package com.shiyi.utils;

import com.google.gson.Gson;
import com.shiyi.common.ApiResult;
import com.shiyi.common.RedisConstants;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.shiyi.entity.SystemConfig;
import com.shiyi.enums.QiNiuAreaEnum;
import com.shiyi.exception.ErrorCode;
import com.shiyi.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class UploadUtil {

    @Autowired
    SystemConfigService systemConfigService;
/*
    @Autowired
    RedisCache redisCache;
*/
    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    private String accessKey;
    private String secretKey;
    private String bucket;
    private Region region;
    private String qiNiuUrl;
    private String localFileUrl;
    private int fileUploadWay;

   /* @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        localFileUrl = systemConfig.getLocalFileUrl();
        fileUploadWay = systemConfig.getFileUploadWay();
        accessKey = systemConfig.getQiNiuAccessKey();
        secretKey = systemConfig.getQiNiuSecretKey();
        bucket = systemConfig.getQiNiuBucket();
        qiNiuUrl = systemConfig.getQiNiuPictureBaseUrl();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());
    }*/



    /**
     * 上传文件
     * @param file
     * @return
     */
    public ApiResult upload(MultipartFile file) {
        log.info("文件上传开始,时间 {}",DateUtils.getNowDate());
        if (file.getSize() > 1024 * 1024 * 10) {
            return ApiResult.fail("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return ApiResult.fail("请选择jpg,jpeg,gif,png格式的图片");
        }

        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        localFileUrl = systemConfig.getLocalFileUrl();
        fileUploadWay = systemConfig.getFileUploadWay();
        accessKey = systemConfig.getQiNiuAccessKey();
        secretKey = systemConfig.getQiNiuSecretKey();
        bucket = systemConfig.getQiNiuBucket();
        qiNiuUrl = systemConfig.getQiNiuPictureBaseUrl();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());

        return fileUploadWay == 0 ? localUpload(file,suffix) : qiNiuUpload(file);
    }

    private ApiResult qiNiuUpload(MultipartFile file) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, null, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
         /*   Set<Object> allImg = redisCache.getCacheSet(RedisConstants.ALL_IMG);
            if (allImg.isEmpty()) allImg = new HashSet<>();
            allImg.add(key);
            redisCache.setCacheSet(RedisConstants.ALL_IMG,allImg);*/
            return ApiResult.success(qiNiuUrl + putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info("QiniuException:{}",r.toString());
            try {
                log.info("QiniuException:{}",r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return ApiResult.fail("七牛云上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResult.fail(ErrorCode.ERROR.getMsg());
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private ApiResult localUpload(MultipartFile file,String suffix){
        String savePath = UPLOAD_FOLDER;
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        //通过UUID生成唯一文件名
        String filename = UUIDUtil.getUuid() + "." + suffix;
        try {
            //将文件保存指定目录
            file.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("保存文件异常");
        }
        //返回文件名称
        return ApiResult.success(localFileUrl + filename);
    }

    /**
     * 批量删除文件
     * @return
     */
    public Boolean delBatchFile(String ...keys) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(bucket, keys);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return false;
        }
    }
}
