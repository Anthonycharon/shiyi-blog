package com.shiyi.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiyi.common.*;
import com.shiyi.dto.SecurityUser;
import com.shiyi.dto.SystemUserDTO;
import com.shiyi.dto.UserInfoDTO;
import com.shiyi.dto.UserOnlineDTO;
import com.shiyi.entity.SystemConfig;
import com.shiyi.entity.User;
import com.shiyi.entity.UserAuth;
import com.shiyi.entity.WebConfig;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.ErrorCode;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.mapper.UserAuthMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.mapper.UserMapper;
import com.shiyi.service.SystemConfigService;
import com.shiyi.service.UserAuthService;
import com.shiyi.service.UserService;
import com.shiyi.service.WebConfigService;
import com.shiyi.strategy.context.SocialLoginStrategyContext;
import com.shiyi.utils.*;
import com.shiyi.vo.EmailLoginVO;
import com.shiyi.vo.EmailRegisterVO;
import com.shiyi.vo.QQLoginVO;
import com.shiyi.vo.UserAuthVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-25
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {
    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private WebConfigService webConfigService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 邮箱账号注册
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult emailRegister(EmailRegisterVO vo) {

        checkEmail(vo.getEmail());

        checkCode(RedisConstants.EMAIL_CODE + vo.getEmail(), vo.getCode());

        User user = getByUserName(vo.getEmail());
        Assert.isNull(user,"该邮箱已注册，请直接登录!");

        WebConfig config = webConfigService.getOne(new QueryWrapper<WebConfig>().last(SysConf.LIMIT_ONE));
        UserAuth auth = UserAuth.builder().email(vo.getEmail()).avatar(config.getTouristAvatar()).nickname(vo.getNickname()).build();
        baseMapper.insert(auth);

        user = User.builder().username(vo.getEmail()).roleId(Constants.USER_ROLE_ID).userAuthId(auth.getId()).loginType(LoginTypeEnum.EMAIL.getType())
                .password(passwordEncoder.encode(vo.getPassword())).build();
        boolean insert = userService.save(user);

        redisCache.deleteObject(RedisConstants.EMAIL_CODE + vo.getEmail());

        return insert  ? ApiResult.ok("注册成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 修改密码
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updatePassword(EmailRegisterVO vo) {

        checkEmail(vo.getEmail());
        checkCode(RedisConstants.EMAIL_CODE + vo.getEmail(), vo.getCode());

        User user = getByUserName(vo.getEmail());
        Assert.notNull(user,"该邮箱未注册!");

        user.setPassword(passwordEncoder.encode(vo.getPassword()));
        boolean update = userService.updateById(user);

        redisCache.deleteObject(RedisConstants.EMAIL_CODE + vo.getEmail());

        return update  ? ApiResult.ok("修改成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 邮箱登录
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult emailLogin(EmailLoginVO vo) {

        checkEmail(vo.getEmail());

        String password = vo.getPassword();
        Assert.isTrue(StringUtils.isNotBlank(password),"密码格式不合法!!");

        User user = getByUserName(vo.getEmail());
        Assert.notNull(user,"邮箱未注册，请先注册在进行登录!!");
        Assert.isTrue(user.getStatus() == Constants.USER_STATUS_ONE,"该邮箱账号已被管理员禁止登录！！");

        boolean validate = passwordEncoder.matches(vo.getPassword(), user.getPassword());
        Assert.isTrue(validate,"密码错误!!");

        UserAuth auth = baseMapper.selectById(user.getUserAuthId());

        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(RedisConstants.LOGIN_PREFIX,user.getUsername());
        String token = jwtUtils.generateToken(claims);
        redisCache.deleteObject(RedisConstants.LOGIN_PREFIX+user.getUsername());
        redisCache.setCacheObject(RedisConstants.LOGIN_PREFIX+user.getUsername(),token,1, TimeUnit.HOURS);

        //组装数据
        UserInfoDTO userInfoDTO = UserInfoDTO.builder().id(user.getId()).userInfoId(auth.getId()).avatar(auth.getAvatar()).nickname(auth.getNickname())
                .intro(auth.getIntro()).webSite(auth.getWebSite()).email(user.getUsername()).loginType(user.getLoginType()).token(token).build();

        //不影响用户登录 新一个线程修改登录时间 ip 地址等信息
        String ip = IpUtils.getIp(request);
        new Thread(() ->{
            user.setIpAddress(ip);
            user.setIpSource(IpUtils.getCityInfo(ip));
            user.setLastLoginTime(DateUtils.getNowDate());
            userService.updateById(user);
        }).start();
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult qqLogin(QQLoginVO qqLoginVO) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult weiboLogin(String code) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(code, LoginTypeEnum.WEIBO);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult giteeLogin(String code) {
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(code, LoginTypeEnum.GITEE);
        return ApiResult.success(userInfoDTO);
    }

    @Override
    public ApiResult logout() {
        String token = request.getHeader(Constants.X_TOKEN);
        Assert.isTrue(StringUtils.isNotBlank(token),"非法TOKEN!!");
        Claims claims = jwtUtils.parseTokenWeb(token);
        String username = (String) claims.get(RedisConstants.LOGIN_PREFIX);
        boolean b = redisCache.deleteObject(RedisConstants.LOGIN_PREFIX + username);
        return b? ApiResult.ok("退出成功"):ApiResult.fail("退出失败");
    }

    @Override
    public ApiResult listOnlineUsers(String keywords,int pageNo,int pageSize) {
        // 获取security在线session
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> !sessionRegistry.getAllSessions(item, false).isEmpty())
                .collect(Collectors.toList());
        List<UserOnlineDTO> userOnlineDTOList = new ArrayList<>();
        for (Object allPrincipal : allPrincipals) {
            SecurityUser securityUser = (SecurityUser)allPrincipal;
            UserOnlineDTO dto = new UserOnlineDTO();
            BeanUtils.copyProperties(securityUser.getCurrentUserInfo(),dto);
            userOnlineDTOList.add(dto);
        }
        userOnlineDTOList = userOnlineDTOList.stream().filter(item -> StringUtils.isBlank(keywords) || item.getNickName().contains(keywords))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = (pageNo-1) * pageSize;
        int toIndex = userOnlineDTOList.size() - fromIndex > pageSize ? fromIndex + pageSize : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex);
        Map<String,Object> map = new HashMap<>();
        map.put(SysConf.RECORDS,userOnlineList);
        map.put(SysConf.TOTALPAGE,userOnlineDTOList.size());
        return ApiResult.success(map);
    }

    /**
     * 用户绑定邮箱，发送验证码
     * @param email
     * @return
     */
    @Override
    public ApiResult sendEmailCode(String email) {
        try {
            emailUtil.sendCode(email);
            return ApiResult.ok("验证码已发送，请前往邮箱查看!!");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ApiResult.ok(ErrorCode.ERROR_DEFAULT.getMsg());
        }

    }

    /**
     * 绑定邮箱
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult bindEmail(UserAuthVO vo) {
        String key = RedisConstants.EMAIL_CODE + vo.getEmail();
        checkCode(key, vo.getCode());

        UserAuth userAuth = getUserAuth();
        userAuth.setEmail(vo.getEmail());
        boolean update = updateById(userAuth);
        redisCache.deleteObject(key);
        return update ? ApiResult.ok("绑定邮箱成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }

    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult updateUser(UserAuthVO vo) {
        UserAuth userAuth = getUserAuth();
        userAuth.setNickname(vo.getNickname());
        userAuth.setWebSite(vo.getWebSite());
        userAuth.setEmail(vo.getEmail());
        userAuth.setIntro(vo.getIntro());
        userAuth.setAvatar(vo.getAvatar());

        boolean update = updateById(userAuth);
        return update ? ApiResult.ok("修改信息成功"):ApiResult.fail(ErrorCode.ERROR_DEFAULT.getMsg());
    }


    //---------------自定义方法开始-------------
    private UserAuth getUserAuth() {
        String token = request.getHeader(Constants.X_TOKEN);
        Assert.isTrue(StringUtils.isNotBlank(token),"非法TOKEN!!");
        Claims claims = jwtUtils.parseTokenWeb(token);
        String userName = (String) claims.get(RedisConstants.LOGIN_PREFIX);
        User user = getByUserName(userName);
        return baseMapper.selectById(user.getUserAuthId());
    }

    public void checkEmail(String email){
        boolean matches = Pattern.compile("\\w+@{1}\\w+\\.{1}\\w+").matcher(email).matches();
        Assert.isTrue(matches,"邮箱格式不合法!!");
    }

    public User getByUserName(String username){
        return userService.getOne(new QueryWrapper<User>().eq(SqlConf.USERNAME, username));
    }

    private void checkCode(String key, String sourCode) {
        Object code = redisCache.getCacheObject(key);
        Assert.isTrue(code != null && code.equals(sourCode), ErrorCode.ERROR_EXCEPTION_MOBILE_CODE.getMsg());
    }
}
