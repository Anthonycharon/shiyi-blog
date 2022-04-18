package com.shiyi.service;

import com.shiyi.common.ResponseResult;
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

    ResponseResult listLog();

    ResponseResult delete(List<Long> ids);
}
