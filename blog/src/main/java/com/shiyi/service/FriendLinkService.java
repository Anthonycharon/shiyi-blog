package com.shiyi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.common.ApiResult;
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

    ApiResult listData(String name, Integer status);

    ApiResult addData(FriendLink friendLink);

    ApiResult updateData(FriendLink friendLink);

    ApiResult delete(List<Integer> ids);

    ApiResult top(Integer id);


    //    ----web端开始-----
    ApiResult webList();

    ApiResult applyFriendLink(FriendLink friendLink);

    ApiResult webSiteInfo();

}
