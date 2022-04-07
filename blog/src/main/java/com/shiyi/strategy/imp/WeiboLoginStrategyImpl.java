package com.shiyi.strategy.imp;

import com.shiyi.config.properties.WeiboConfigProperties;
import com.shiyi.dto.SocialTokenDTO;
import com.shiyi.dto.SocialUserInfoDTO;
import com.shiyi.dto.WeiboTokenDTO;
import com.shiyi.dto.WeiboUserInfoDTO;
import com.shiyi.common.ResultCode;
import com.shiyi.common.SocialLoginConst;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微博登录策略实现
 *
 * @author blue
 * @date 2021/07/28
 */
@Service("weiboLoginStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeiboLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    private static final Logger logger = LoggerFactory.getLogger(WeiboLoginStrategyImpl.class);

    private final RestTemplate restTemplate;

    private final WeiboConfigProperties weiboConfigProperties;

    @Override
    public SocialTokenDTO getSocialToken(String code) {
        // 获取微博token信息
        WeiboTokenDTO weiboToken = getWeiboToken(code);
        logger.info("weibo login as weiboToken :{}",weiboToken.toString());
        // 返回token信息
        return SocialTokenDTO.builder()
                .openId(weiboToken.getUid())
                .accessToken(weiboToken.getAccess_token())
                .loginType(LoginTypeEnum.WEIBO.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO) {
        // 定义请求参数
        Map<String, String> data = new HashMap<>(2);
        data.put(SocialLoginConst.UID, socialTokenDTO.getOpenId());
        data.put(SocialLoginConst.ACCESS_TOKEN, socialTokenDTO.getAccessToken());
        // 获取微博用户信息
        WeiboUserInfoDTO weiboUserInfoDTO = restTemplate.getForObject(weiboConfigProperties.getUserInfoUrl(), WeiboUserInfoDTO.class, data);
        logger.info("weibo login as info :{}",weiboUserInfoDTO.toString());
        // 返回用户信息
        return SocialUserInfoDTO.builder()
                .nickname(Objects.requireNonNull(weiboUserInfoDTO).getScreen_name())
                .avatar(weiboUserInfoDTO.getAvatar_hd())
                .build();
    }

    /**
     * 获取微博token信息
     *
     * @param code 微博code
     * @return {@link WeiboTokenDTO} 微博token
     */
    private WeiboTokenDTO getWeiboToken(String code) {

        // 根据code换取微博uid和accessToken
        MultiValueMap<String, String> weiboData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        weiboData.add(SocialLoginConst.CLIENT_ID, weiboConfigProperties.getAppId());
        weiboData.add(SocialLoginConst.CLIENT_SECRET, weiboConfigProperties.getAppSecret());
        weiboData.add(SocialLoginConst.GRANT_TYPE, weiboConfigProperties.getGrantType());
        weiboData.add(SocialLoginConst.REDIRECT_URI, weiboConfigProperties.getRedirectUrl());
        weiboData.add(SocialLoginConst.CODE, code);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(weiboData, null);
        try {
            return restTemplate.exchange(weiboConfigProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, WeiboTokenDTO.class).getBody();
        } catch (Exception e) {
            throw new BusinessException(ResultCode.WEIBO_LOGIN_ERROR.getDesc());
        }
    }

}

