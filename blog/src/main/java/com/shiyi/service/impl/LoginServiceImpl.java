package com.shiyi.service.impl;

import com.google.code.kaptcha.Producer;
import com.shiyi.service.LoginService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author blue
 * @description:
 * @date 2021/7/30 14:59
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Override
    public Map<String, String> getCode(HttpServletResponse response) throws IOException {
        Map<String, String> result = new HashMap<String,String>();
      /*  // 生成验证码的UUID
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
        result.put("img", Base64.encode(os.toByteArray()));*/
        return result;
    }
}
