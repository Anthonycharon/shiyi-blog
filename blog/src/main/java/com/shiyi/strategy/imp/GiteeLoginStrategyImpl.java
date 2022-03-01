package com.shiyi.strategy.imp;

import com.shiyi.config.GiteeConfigProperties;
import com.shiyi.dto.*;
import com.shiyi.common.ResultCode;
import com.shiyi.common.SocialLoginConst;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
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
@Service("giteeLoginStrategyImpl")
@Slf4j
public class GiteeLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GiteeConfigProperties giteeConfigProperties;

    @Override
    public SocialTokenDTO getSocialToken(String code) {
        // 获取码云token信息
        String token  = getWeiboToken(code);
        log.info("gitee login as giteeToken :{}",token);
        // 返回token信息
        return SocialTokenDTO.builder()
                .openId(null)
                .accessToken(token)
                .loginType(LoginTypeEnum.GITEE.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO) {
        // 定义请求参数
        Map<String, String> data = new HashMap<>(1);
        data.put(SocialLoginConst.ACCESS_TOKEN, socialTokenDTO.getAccessToken());
        // 获取码云用户信息
        GiteeUserInfoDTO giteeUserInfoDTO = restTemplate.getForObject(giteeConfigProperties.getUserInfoUrl(), GiteeUserInfoDTO.class, data);
        log.info("gitee login as info :{}",giteeUserInfoDTO.toString());
        // 返回用户信息
        return SocialUserInfoDTO.builder()
                .id(Objects.requireNonNull(giteeUserInfoDTO).getId())
                .nickname(Objects.requireNonNull(giteeUserInfoDTO).getName())
                .avatar(giteeUserInfoDTO.getAvatar_url())
                .build();
    }

    /**
     * 获取码云token信息
     *
     * @param code 码云code
     * @return {@link WeiboTokenDTO} 码云token
     */
    private String getWeiboToken(String code) {

        // 根据code换取accessToken
        MultiValueMap<String, String> weiboData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        weiboData.add(SocialLoginConst.CLIENT_ID, giteeConfigProperties.getAppId());
        weiboData.add(SocialLoginConst.CLIENT_SECRET, giteeConfigProperties.getAppSecret());
        weiboData.add(SocialLoginConst.GRANT_TYPE, giteeConfigProperties.getGrantType());
        weiboData.add(SocialLoginConst.REDIRECT_URI, giteeConfigProperties.getRedirectUrl());
        weiboData.add(SocialLoginConst.CODE, code);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(weiboData, null);
        try {
            Map map  = restTemplate.exchange(giteeConfigProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, Map.class).getBody();
            String accessToken =map.get("access_token").toString();
            return accessToken;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.WEIBO_LOGIN_ERROR.getDesc());
        }
    }

}

