package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ResponseResult;
import com.shiyi.common.SqlConf;
import com.shiyi.service.EmailService;
import com.shiyi.vo.FriendLinkVO;
import com.shiyi.entity.FriendLink;
import com.shiyi.mapper.FriendLinkMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.service.FriendLinkService;
import com.shiyi.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.shiyi.enums.FriendLinkEnum.APPLY;
import static com.shiyi.enums.FriendLinkEnum.UP;

/**
 * <p>
 * 友情链接表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    private final EmailService emailService;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 友链列表
     * @param name
     * @param status
     * @return
     */
    @Override
    public ResponseResult selectFriendLink(String name, Integer status) {
        QueryWrapper<FriendLink> queryWrapper= new QueryWrapper<FriendLink>()
                .orderByDesc(SqlConf.SORT).like(StringUtils.isNotBlank(name),SqlConf.NAME,name)
                .eq(status != null,SqlConf.STATUS,status);
        Page<FriendLink> friendLinkPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return ResponseResult.success(friendLinkPage);
    }

    /**
     * 添加友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertFriendLink(FriendLink friendLink) {
        baseMapper.insert(friendLink);
        return ResponseResult.success();
    }

    /**
     * 修改友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateFriendLink(FriendLink friendLink) {
        baseMapper.updateById(friendLink);
        //审核通过发送邮件通知
        if(friendLink.getStatus().equals(UP.getCode())){
            emailService.friendPassSendEmail(friendLink.getEmail());
        }
        return ResponseResult.success();
    }

    /**
     * 删除友链
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("删除友链失败");
    }

    /**
     * 置顶友链
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Integer id) {
        Integer sort = baseMapper.getMaxSort();
        baseMapper.top(id,sort+1);
        return ResponseResult.success();
    }

    //    ---------web端方法开始------
    /**
     * 友链列表
     * @return
     */
    @Override
    public ResponseResult webFriendLinkList() {
        List<FriendLinkVO> list = baseMapper.selectLinkList(UP.code);
        return ResponseResult.success(list);
    }

    /**
     * 申请友链
     * @param friendLink
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult applyFriendLink(FriendLink friendLink) {

        Assert.isTrue(StringUtils.isNotBlank(friendLink.getUrl()),"输入正确的网址!");
        friendLink.setStatus(APPLY.getCode());

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
        threadPoolTaskExecutor.execute(() -> emailService.emailNoticeMe("友链接入通知","网站有新的友链接入啦，快去审核吧!!!"));
        return ResponseResult.success();
    }
}
