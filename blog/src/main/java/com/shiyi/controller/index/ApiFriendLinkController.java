package com.shiyi.controller.index;


import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.FriendLink;
import com.shiyi.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/web/friend")
@Api(tags = "友情链接-接口")
public class ApiFriendLinkController {
    @Autowired
    private FriendLinkService friendLinkService;


    @BusinessLog(value = "友链模块-用户申请友链",type = "添加",desc = "用户申请友链")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "申请友链", httpMethod = "POST", response = ApiResult.class, notes = "申请友链")
    @IgnoreUrl
    public ApiResult addLink(@RequestBody FriendLink friendLink){
        return friendLinkService.applyFriendLink(friendLink);
    }

    @BusinessLog(value = "友链模块-用户访问页面",type = "查询",desc = "友链列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "友链列表", httpMethod = "POST", response = ApiResult.class, notes = "友链列表")
    @IgnoreUrl
    public ApiResult query(){
        return friendLinkService.webList();
    }

    @RequestMapping(value = "/webSiteInfo",method = RequestMethod.POST)
    @ApiOperation(value = "友链网站信息", httpMethod = "POST", response = ApiResult.class, notes = "友链网站信息")
    @IgnoreUrl
    public ApiResult webSiteInfo(){
        return friendLinkService.webSiteInfo();
    }
}

