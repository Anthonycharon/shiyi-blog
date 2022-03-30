package com.shiyi.service;

import com.shiyi.common.ApiResult;
import com.shiyi.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
public interface MessageService extends IService<Message> {

    ApiResult listData(String name);

    ApiResult deleteById(int id);

    ApiResult passBatch(List<Integer> ids);

    ApiResult deleteBatch(List<Integer> ids);




    //    ------web端方法开始-----
    ApiResult webAddMessage(Message message);

    ApiResult webMessage();


}
