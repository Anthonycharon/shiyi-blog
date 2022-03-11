package com.shiyi.config.satoken;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.shiyi.mapper.UserMapper;
import com.shiyi.utils.DateUtils;
import com.shiyi.utils.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 自定义侦听器的实现
 */
@Component
@Slf4j
public class MySaTokenListener implements SaTokenListener {

    public static final List<OnlineUser> ONLINE_USERS = new CopyOnWriteArrayList<>();
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        initRefreshThread();
    }

    /** 每次登录时触发 */
    @Override
    public void doLogin(String loginType, Object loginId, SaLoginModel loginModel) {
        //修改登录信息
        String ip = IpUtils.getIp(request);
        String cityInfo = IpUtils.getCityInfo(ip);
        userMapper.updateLoginInfo(loginId,ip,cityInfo);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        //StpUtil.getTokenValue()不知道为什么获取到的是null 所以只能调用这个方法来获取token
        String token = StpUtil.getTokenValueByLoginId(loginId);
        ONLINE_USERS.add(OnlineUser.builder()
                .ip(ip)
                .city(cityInfo)
                .loginTime(DateUtils.getNowDate())
                .os(userAgent.getOperatingSystem().getName())
                .userId((Long) loginId)
                .tokenValue(token)
                .nickName(userMapper.getById(loginId).getNickname())
                .browser(userAgent.getBrowser().getName()).build());
        log.info("user doLogin,useId:{},token:{}", loginId, StpUtil.getTokenValue());
        // ...
    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        // ...
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        log.info("user doLogout,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被踢下线时触发 */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        // ...
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        log.info("user doKickout,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        // ...
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        log.info("user doReplaced,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {
        // ...
    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {
        // ...
    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {
        // ...
    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {
        // ...
        log.info("user doLogoutSession,id:{}", id);
    }

    // --------------------- 定时清理过期数据

    /**
     * 执行数据清理的线程
     */
    public Thread refreshThread;

    /**
     * 是否继续执行数据清理的线程标记
     */
    public boolean refreshFlag;

    /**
     * 初始化定时任务
     */
    public void initRefreshThread() {

        // 如果配置了<=0的值，则不启动定时清理
        if (SaManager.getConfig().getDataRefreshPeriod() <= 0) {
            return;
        }
        // 启动定时刷新
        this.refreshFlag = true;
        this.refreshThread = new Thread(() -> {
            for (; ; ) {
                log.info("定时清理过期会话开始。间隔：{}s,在线人数：{}", SaManager.getConfig().getDataRefreshPeriod() + 5, ONLINE_USERS.size());
                try {
                    try {
                        // 如果已经被标记为结束
                        if (refreshFlag == false) {
                            return;
                        }
                        long start = System.currentTimeMillis();
                        ONLINE_USERS.removeIf(onlineUser -> {
                            long timeout = StpUtil.stpLogic.getTokenActivityTimeoutByToken(onlineUser.getTokenValue());
                            if (timeout == SaTokenDao.NOT_VALUE_EXPIRE) {
                                return true;
                            }
                            return false;
                        });
                        log.info("定时清理过期会话结束，在线人数：{},耗时：{}ms", ONLINE_USERS.size(), System.currentTimeMillis() - start);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 休眠N秒
                    int dataRefreshPeriod = SaManager.getConfig().getDataRefreshPeriod();
                    if (dataRefreshPeriod <= 0) {
                        dataRefreshPeriod = 1;
                    }
                    dataRefreshPeriod = dataRefreshPeriod + 5;
                    Thread.sleep(dataRefreshPeriod * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        refreshThread.start();
    }

}