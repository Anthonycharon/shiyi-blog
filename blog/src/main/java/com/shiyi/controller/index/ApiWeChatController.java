package com.shiyi.controller.index;

import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.common.RedisConstants;
import com.shiyi.utils.RandomUtil;
import com.shiyi.utils.RedisCache;
import com.shiyi.utils.WeChatUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author blue
 * @date 2022/1/11
 * @apiNote
 */
@Slf4j
@Api(tags = "微信接口相关控制器")
@RestController
public class ApiWeChatController {

    @Autowired
    private RedisCache redisCache;

    @ApiOperation("微信公众号服务器配置校验token")
    @RequestMapping(value = "/wechat",method = RequestMethod.GET)
    @IgnoreUrl
    public void checkToken(HttpServletRequest request, HttpServletResponse response) {
        //token验证代码段
        try {
            log.info("请求已到达，开始校验token");
            if (StringUtils.isNotBlank(request.getParameter("signature"))) {
                String signature = request.getParameter("signature");
                String timestamp = request.getParameter("timestamp");
                String nonce = request.getParameter("nonce");
                String echostr = request.getParameter("echostr");
                log.info("signature[{}], timestamp[{}], nonce[{}], echostr[{}]", signature, timestamp, nonce, echostr);
                if (WeChatUtil.checkSignature(signature, timestamp, nonce)) {
                    log.info("数据源为微信后台，将echostr[{}]返回！", echostr);
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    out.write(echostr.getBytes());
                    out.flush();
                    out.close();
                }
            }
        } catch (IOException e) {
            log.error("校验出错");
            e.printStackTrace();
        }
    }


    @ApiOperation("处理微信服务器的消息转发")
    @PostMapping(value = "wechat")
    @IgnoreUrl
    public String  wechat(HttpServletRequest request) throws Exception {
        // 调用parseXml方法解析请求消息
        Map<String,String> requestMap = WeChatUtil.parseXml(request);
        // 消息类型
        String msgType = requestMap.get("MsgType");
        // xml格式的消息数据
        String respXml = null;
        String mes = requestMap.get("Content");
        // 文本消息
        if ("text".equals(msgType) && "验证码".equals(mes)) {
            String code = RandomUtil.generationNumber(6);
            respXml=WeChatUtil.sendTextMsg(requestMap,code);
            redisCache.setCacheObject(RedisConstants.WECHAT_CODE+code,code,30, TimeUnit.MINUTES);
        }
        return respXml;
    }
}
