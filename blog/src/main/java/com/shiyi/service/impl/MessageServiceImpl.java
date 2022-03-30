package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.common.ApiResult;
import com.shiyi.common.SqlConf;
import com.shiyi.entity.Message;
import com.shiyi.mapper.MessageMapper;
import com.shiyi.service.MessageService;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.IpUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-09-03
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    private final HttpServletRequest request;

    /**
     * 留言列表
     * @param name
     * @return
     */
    @Override
    public ApiResult listData(String name) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<Message>()
                .like(StringUtils.isNotBlank(name),SqlConf.NICKNAME,name);
        Page<Message> list = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),queryWrapper);
        return ApiResult.success(list);
    }

    /**
     * 批量通过留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult passBatch(List<Integer> ids) {
        Assert.notEmpty(ids,"参数不合法!");
        baseMapper.passBatch(ids);
        return ApiResult.ok();
    }

    /**
     * 删除留言
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteById(int id) {
        baseMapper.deleteById(id);
        return ApiResult.ok();
    }

    /**
     * 批量删除留言
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult deleteBatch(List<Integer> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ?ApiResult.ok():ApiResult.fail("批量删除留言失败");
    }



                //    -------web端方法开始-------
    /**
     * 留言列表
     * @return
     */
    @Override
    public ApiResult webMessage() {
        // 查询留言列表
        List<Message> messageList = baseMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getContent, Message::getTime));
        return ApiResult.success(messageList);
    }

    /**
     * 添加留言
     * @param message
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult webAddMessage(Message message) {
        // 获取用户ip
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getCityInfo(ipAddress);
        message.setIpAddress(ipAddress);
        message.setIpSource(ipSource);
        baseMapper.insert(message);
        return ApiResult.ok("留言成功");
    }


}
