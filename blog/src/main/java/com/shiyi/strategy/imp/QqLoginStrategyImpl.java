package com.shiyi.strategy.imp;

import com.alibaba.fastjson.JSON;

import com.shiyi.config.properties.QqConfigProperties;
import com.shiyi.dto.QQTokenDTO;
import com.shiyi.dto.QQUserInfoDTO;
import com.shiyi.dto.SocialTokenDTO;
import com.shiyi.dto.SocialUserInfoDTO;
import com.shiyi.common.ResultCode;
import com.shiyi.common.SocialLoginConst;
import com.shiyi.enums.LoginTypeEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.vo.QQLoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * qq登录策略实现
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service("qqLoginStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QqLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    private final QqConfigProperties qqConfigProperties;

    private final RestTemplate restTemplate;

    @Override
    public SocialTokenDTO getSocialToken(String data) {
        QQLoginVO qqLoginVO = JSON.parseObject(data, QQLoginVO.class);
        // 校验QQ token信息
        checkQQToken(qqLoginVO);
        // 返回token信息
        return SocialTokenDTO.builder()
                .openId(qqLoginVO.getOpenId())
                .accessToken(qqLoginVO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO) {
        // 定义请求参数
        Map<String, String> formData = new HashMap<>(3);
        formData.put(SocialLoginConst.QQ_OPEN_ID, socialTokenDTO.getOpenId());
        formData.put(SocialLoginConst.ACCESS_TOKEN, socialTokenDTO.getAccessToken());
        formData.put(SocialLoginConst.OAUTH_CONSUMER_KEY, qqConfigProperties.getAppId());
        // 获取QQ返回的用户信息
        QQUserInfoDTO qqUserInfoDTO = JSON.parseObject(restTemplate.getForObject(qqConfigProperties.getUserInfoUrl(), String.class, formData), QQUserInfoDTO.class);
        // 返回用户信息
        return SocialUserInfoDTO.builder()
                .nickname(Objects.requireNonNull(qqUserInfoDTO).getNickname())
                .avatar(qqUserInfoDTO.getFigureurl_qq_1())
                .build();
    }

    /**
     * 校验qq token信息
     *
     * @param qqLoginVO qq登录信息
     */
    private void checkQQToken(QQLoginVO qqLoginVO) {
        // 根据token获取qq openId信息
        Map<String, String> qqData = new HashMap<>(1);
        qqData.put(SocialLoginConst.ACCESS_TOKEN, qqLoginVO.getAccessToken());
        try {
            String result = restTemplate.getForObject(qqConfigProperties.getCheckTokenUrl(), String.class, qqData);
            QQTokenDTO qqTokenDTO = JSON.parseObject(getBracketsContent(Objects.requireNonNull(result)), QQTokenDTO.class);
            // 判断openId是否一致
            if (!qqLoginVO.getOpenId().equals(qqTokenDTO.getOpenid())) {
                throw new BusinessException(ResultCode.QQ_LOGIN_ERROR.getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.QQ_LOGIN_ERROR.getDesc());
        }
    }

    /**
     * 获取括号内容
     *
     * @param str str
     * @return {@link String} 括号内容
     */
    public static String getBracketsContent(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }
}
