package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.UserLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-11-09
 */
public interface UserLogService extends IService<UserLog> {

    ApiResult listData(Integer pageNo, Integer pageSize);

    ApiResult delete(List<Long> ids);
}
