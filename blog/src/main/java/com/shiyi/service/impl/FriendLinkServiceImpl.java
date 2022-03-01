package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.common.SysConf;
import com.shiyi.entity.FriendLink;
import com.shiyi.entity.WebConfig;
import com.shiyi.enums.FriendLinkEnum;
import com.shiyi.mapper.FriendLinkMapper;
import com.shiyi.service.WebConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.service.FriendLinkService;
import com.shiyi.utils.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    private WebConfigService webConfigService;
    @Autowired
    EmailUtil emailUtil;

    /**
     * 友链列表
     * @param name
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public ApiResult listData(String name, Integer status, Integer pageNo, Integer pageSize) {
        QueryWrapper<FriendLink> queryWrapper= new QueryWrapper<FriendLink>()
                .orderByDesc(SqlConf.SORT).like(StringUtils.isNotBlank(name),SqlConf.NAME,name)
                .eq(status != null,SqlConf.STATUS,status);
        Page<FriendLink> friendLinkPage = baseMapper.selectPage(new Page<>(pageNo,pageSize),queryWrapper);
        return ApiResult.success(friendLinkPage);
    }

    /**
     * 添加友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult addData(FriendLink friendLink) {
        baseMapper.insert(friendLink);
        return ApiResult.ok();
    }

    /**
     * 修改友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateData(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        //审核通过发送邮件通知
        if(friendLink.getStatus().equals(FriendLinkEnum.PASS.getCode())){
            emailUtil.friendPassSendEmail(friendLink.getEmail());
        }
        return ApiResult.ok();
    }

    /**
     * 删除友链
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult delete(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ApiResult.ok():ApiResult.fail("删除友链失败");
    }

    /**
     * 置顶友链
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id,sort+1);
        return ApiResult.ok();
    }

    //    ---------web端方法开始------

    /**
     * 友链列表
     * @return
     */
    @Override
    public ApiResult webList() {
        QueryWrapper<FriendLink> queryWrapper = new QueryWrapper<FriendLink>()
                .eq(SqlConf.STATUS, FriendLinkEnum.PASS.getCode())
                .orderByDesc(SqlConf.SORT);
        List<FriendLink> list = baseMapper.selectList(queryWrapper);
        return ApiResult.success(list);
    }

    /**
     * 申请友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult applyFriendLink(FriendLink friendLink) {

        Assert.isTrue(StringUtils.isNotBlank(friendLink.getUrl()),"输入正确的网址!");
        friendLink.setStatus(FriendLinkEnum.APPLY.getCode());

        //如果已经申请过友链 再次接入则会进行下架处理 需重新审核
        FriendLink entity = baseMapper.selectOne(new QueryWrapper<FriendLink>()
                .eq(SqlConf.URL,friendLink.getUrl()));
        if (entity != null) {
            friendLink.setId(entity.getId());
            baseMapper.updateById(friendLink);
        }else {
            baseMapper.insert(friendLink);
        }

        //不影响用户体验 新一个线程操作邮箱发送
        new Thread(() -> emailUtil.emailNoticeMe("友链接入通知","网站有新的友链接入啦，快去审核吧!!!"));
        return ApiResult.ok();
    }

    /**
     * 获取网站信息
     * @return
     */
    @Override
    public ApiResult webSiteInfo() {
        WebConfig webConfig = webConfigService.getOne(new QueryWrapper<WebConfig>()
                .last(SysConf.LIMIT_ONE));
        return ApiResult.success(webConfig);
    }
}
