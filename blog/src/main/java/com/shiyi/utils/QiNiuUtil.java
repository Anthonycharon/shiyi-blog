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
import com.shiyi.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class QiNiuUtil {

    @Autowired
    SystemConfigService systemConfigService;
/*
    @Autowired
    RedisCache redisCache;
*/

    private String accessKey;

    private String secretKey;

    private String bucket;

    private Region region;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        accessKey = systemConfig.getQiNiuAccessKey();
        secretKey = systemConfig.getQiNiuSecretKey();
        bucket = systemConfig.getQiNiuBucket();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());
    }



    /**
     * 上传文件
     * @param file
     * @return
     */
    public ApiResult upload(MultipartFile file) {

        log.info("文件上传开始--------");
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
/*        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String key = new Date().getTime() + "." + suffix;*/
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, null, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("文件上传成功 结束-----");
         /*   Set<Object> allImg = redisCache.getCacheSet(RedisConstants.ALL_IMG);
            if (allImg.isEmpty()) allImg = new HashSet<>();
            allImg.add(key);
            redisCache.setCacheSet(RedisConstants.ALL_IMG,allImg);*/
            return ApiResult.success(putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.info("QiniuException:{}",r.toString());
            try {
                log.info("QiniuException:{}",r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return ApiResult.fail("上传失败");
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResult.fail("上传失败");
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
