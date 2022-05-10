package com.shiyi.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.shiyi.common.ResponseResult;
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
import com.shiyi.enums.FileUploadWayEnum;
import com.shiyi.enums.QiNiuAreaEnum;
import com.shiyi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.shiyi.common.ResultCode.FILE_UPLOAD_ERROR;
import static com.shiyi.common.ResultCode.FILE_UPLOAD_WAY_ERROR;
import static com.shiyi.enums.FileUploadWayEnum.*;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    private final SystemConfigService systemConfigService;

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    private String qi_niu_accessKey;
    private String qi_niu_secretKey;
    private String qi_niu_bucket;
    private Region region;
    private String qi_niu_url;
    private String ali_accessKey;
    private String ali_secretKey;
    private String ali_endpoint;
    private String ali_bucket;
    private String localFileUrl;
    private int fileUploadWay;

    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        localFileUrl = systemConfig.getLocalFileUrl();
        fileUploadWay = systemConfig.getFileUploadWay();
        qi_niu_accessKey = systemConfig.getQiNiuAccessKey();
        qi_niu_secretKey = systemConfig.getQiNiuSecretKey();
        qi_niu_bucket = systemConfig.getQiNiuBucket();
        qi_niu_url = systemConfig.getQiNiuPictureBaseUrl();
        region = QiNiuAreaEnum.getRegion(systemConfig.getQiNiuArea());
        ali_accessKey = systemConfig.getAliYunAccessKey();
        ali_secretKey = systemConfig.getAliYunSecretKey();
        ali_endpoint = systemConfig.getAliYunEndpoint();
        ali_bucket = systemConfig.getAliYunBucket();
    }



    /**
     * 上传文件
     * @param file
     * @return
     */
    public ResponseResult upload(MultipartFile file) {
        logger.info("文件上传开始,时间 {}",DateUtils.getNowDate());
        if (file.getSize() > 1024 * 1024 * 10) {
            return ResponseResult.error("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return ResponseResult.error("请选择jpg,jpeg,gif,png格式的图片");
        }
        return selectFileUploadWay(fileUploadWay,file,suffix);
    }

    /**
     * 七牛云上传
     * @param file
     * @param suffix
     * @return
     */
    private ResponseResult qiNiuUpload(MultipartFile file, String suffix) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(region);
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        String upToken = auth.uploadToken(qi_niu_bucket);
        FileInputStream inputStream = null;
        try {
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, UUIDUtil.getUuid() + suffix, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(),DefaultPutRet.class);
            return ResponseResult.success(qi_niu_url + putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.info("QiniuException:{}",r.toString());
            return ResponseResult.error(FILE_UPLOAD_ERROR.getDesc());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error();
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
     * 文件上传 -- 阿里云
     *
     * @param file   文件
     * @param suffix 文件后缀名
     * @return ResponseResult
     */
    private ResponseResult aliYunUpload(MultipartFile file, String suffix) {

        ClientBuilderConfiguration conf = getClientBuilderConfiguration();

        // 创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(ali_endpoint, ali_accessKey, ali_secretKey, conf);

        //获取上传文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            //在文件名称里面添加随机唯一值（因为如果上传文件名称相同的话，后面的文件会将前面的文件给覆盖了）
            String newFileName = UUIDUtil.getUuid() + "." + suffix;

            // 调用oss方法实现上传
            // 参数一：Bucket名称  参数二：上传到oss文件路径和文件名称  比如 /aa/bb/1.jpg 或者直接使用文件名称  参数三：上传文件的流
            ossClient.putObject(ali_bucket, newFileName, inputStream);

            //把上传之后的文件路径返回，需要把上传到阿里云路径返回    https://edu-guli-eric.oss-cn-beijing.aliyuncs.com/1.jpg
            String aliyunUrl = " https://" + ali_bucket + "." + ali_endpoint + "/" + newFileName;
            return ResponseResult.success(aliyunUrl);
        } catch (IOException e) {
            e.printStackTrace();
            //返回文件名称
            return ResponseResult.error(FILE_UPLOAD_ERROR.getDesc());
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 本地上传
     * @param file
     * @param suffix
     * @return
     */
    private ResponseResult localUpload(MultipartFile file, String suffix){
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
            return ResponseResult.error(FILE_UPLOAD_ERROR.getDesc());
        }
        //返回文件名称
        return ResponseResult.success(localFileUrl + filename);
    }

    /**
     * 批量删除文件
     * @return
     */
    public Boolean delBatchFile(String ...keys) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        //...其他参数参考类注释
        Auth auth = Auth.create(qi_niu_accessKey, qi_niu_secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(qi_niu_bucket, keys);
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

    /**
     * 删除单个文件 -- 阿里云OSS
     *
     * @param key   文件url
     * @return      ResponseResult
     */
    public Boolean aliYunDelFile(String key) {

        // 创建OSS实例
        OSS ossClient = new OSSClientBuilder().build(ali_endpoint, ali_accessKey, ali_secretKey);

        try {
            // 判断文件是否存在。如果返回值为true，则文件存在，否则存储空间或者文件不存在。
            boolean found = ossClient.doesObjectExist(ali_bucket, key);
            if (found) {
                // 文件存在，删除文件
                ossClient.deleteObject(ali_bucket, key);
                return true;
            } else {
                return false;
            }
        } catch (OSSException oe) {
            // 获取OSS异常
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return false;
        } finally {
            if (ossClient != null) {
                // 最终一定要执行关闭OSSClient
                ossClient.shutdown();
            }
        }

    }

    /**
     * 选择文件上传方式
     *
     * @param fileUploadWay 上传方式 --> 0:本地   1:七牛云  2:阿里云
     * @param file          文件
     * @param suffix        文件后缀名
     * @return ResponseResult
     */
    private ResponseResult selectFileUploadWay(Integer fileUploadWay, MultipartFile file, String suffix) {
        // 选择对应的文件上传方式
        if (fileUploadWay == LOCAL.code) {
            // 0:本地
            return localUpload(file, suffix);
        } else if (fileUploadWay == QI_NIU.code) {
            // 1:七牛云
            return qiNiuUpload(file, suffix);
        } else if (fileUploadWay == ALI.code) {
            // 2:阿里云
            return aliYunUpload(file, suffix);
        }
        return ResponseResult.error(FILE_UPLOAD_WAY_ERROR.getDesc());
    }

    @NotNull
    private ClientBuilderConfiguration getClientBuilderConfiguration() {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(60000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(3);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(false);
        return conf;
    }
}
