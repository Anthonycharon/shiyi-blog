package com.shiyi.utils;


import com.shiyi.common.ResponseResult;

import com.shiyi.enums.FileUploadModelEnum;
import com.shiyi.service.SystemConfigService;
import com.shiyi.strategy.context.FileUploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UploadUtil {

    private static final Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    private final SystemConfigService systemConfigService;

    private final FileUploadStrategyContext fileUploadStrategyContext;

    private String strategy;

    /**
     * 上传文件
     * @param file
     * @return
     */
    public ResponseResult upload(MultipartFile file) {
        if (file.getSize() > 1024 * 1024 * 10) {
            return ResponseResult.error("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return ResponseResult.error("请选择jpg,jpeg,gif,png格式的图片");
        }
        getFileUploadWay();
        String key = fileUploadStrategyContext.executeFileUploadStrategy(strategy, file, suffix);
        return ResponseResult.success(key);
    }



    public ResponseResult delBatchFile(String ...key) {
        getFileUploadWay();
        Boolean isSuccess = fileUploadStrategyContext.executeDeleteFileStrategy(strategy, key);
        if (!isSuccess) {
            return ResponseResult.error("删除文件失败");
        }
        return ResponseResult.success();
    }

    private void getFileUploadWay() {
        strategy = FileUploadModelEnum.getStrategy(systemConfigService.getCustomizeOne().getFileUploadWay());
    }
}
