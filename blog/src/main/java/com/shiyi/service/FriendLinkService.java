package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.FriendLink;

import java.util.List;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface FriendLinkService extends IService<FriendLink> {

    ResponseResult listData(String name, Integer status);

    ResponseResult addData(FriendLink friendLink);

    ResponseResult updateData(FriendLink friendLink);

    ResponseResult delete(List<Integer> ids);

    ResponseResult top(Integer id);


    //    ----web端开始-----
    ResponseResult webFriendLinkList();

    ResponseResult applyFriendLink(FriendLink friendLink);


}
