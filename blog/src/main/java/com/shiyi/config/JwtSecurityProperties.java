package com.shiyi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author blue
 * @description: JWT基础类
 * @date 2021/7/19 15:14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtSecurityProperties {
    /** Request Headers ： Authorization */
    private String header;

    /** 令牌前缀，最后留个空格 Bearer */
    private String tokenStartWith;

    /** Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位/毫秒 */
    private Long tokenValidityInSeconds;

    /**返回令牌前缀 */
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
