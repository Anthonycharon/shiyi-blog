package com.shiyi.service.impl;

import com.google.code.kaptcha.Producer;
import com.shiyi.common.RedisConstants;
import com.shiyi.mapper.RoleMapper;
import com.shiyi.mapper.UserMapper;
import com.shiyi.mapper.UserRoleMapper;
import com.shiyi.service.LoginService;
import com.shiyi.utils.Base64;
import com.shiyi.utils.RedisCache;
import com.shiyi.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:59
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Map<String, String> getCode(HttpServletResponse response) throws IOException {
        Map<String, String> result = new HashMap<String,String>();
        // 生成验证码的UUID
        String uuid = UUIDUtil.getUuid();
        String capStr = null;
        String code = null;
        BufferedImage image = null;
        // 生成验证码
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        redisCache.setCacheObject(RedisConstants.CAPTCHA_CODE+uuid,code,RedisConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        result.put("uuid",uuid);
        result.put("img", Base64.encode(os.toByteArray()));
        return result;
    }
}
