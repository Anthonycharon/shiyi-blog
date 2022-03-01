package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站配置表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-27
 */
public interface WebConfigService extends IService<WebConfig> {

    ApiResult listData();

    ApiResult updateData(WebConfig webConfig);
}
