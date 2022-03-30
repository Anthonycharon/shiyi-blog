package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.AdminLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
public interface AdminLogService extends IService<AdminLog> {

    ApiResult listLog();

    ApiResult delete(List<Long> ids);
}
