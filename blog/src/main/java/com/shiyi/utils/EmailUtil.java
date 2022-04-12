package com.shiyi.utils;

import com.shiyi.common.RedisConstants;
import com.shiyi.entity.SystemConfig;
import com.shiyi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    private final RedisCache redisCache;

    private final SystemConfigService systemConfigService;

    JavaMailSenderImpl javaMailSender;

    @PostConstruct
    public void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(systemConfig.getEmailHost());
        javaMailSender.setUsername(systemConfig.getEmailUsername());
        javaMailSender.setPassword(systemConfig.getEmailPassword());
        javaMailSender.setPort(systemConfig.getEmailPort());
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.debug", "true");
        javaMailSender.setJavaMailProperties(p);
    }


    /**
     * 通知我
     */
    public void emailNoticeMe(String subject,String content) {

        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件主题
        message.setSubject(subject);
        // 设置邮件发送者
        message.setFrom(javaMailSender.getUsername());
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开
        message.setTo("1248954763@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
    }

    /**
     * 发送邮箱验证码
     */
    public void sendCode(String email) throws MessagingException {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = "<html>\n" +
                "<body>\n" +
                "    <span>您正在<a href='http://www.shiyit.com'>拾壹博客</a>使用邮箱验证，验证码 | <span style='color:blue'>"+code+"</span>,有效期<span style='color:grey'>300s</span>。</span>\n" +
                "</body>\n" +
                "</html>";
        //创建一个MINE消息
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(mimeMessage, true);
        // 设置邮件主题
        minehelper.setSubject("拾壹博客邮箱验证");
        // 设置邮件发送者
        minehelper.setFrom(javaMailSender.getUsername());
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开
        minehelper.setTo(email);
        // 设置邮件发送日期
        minehelper.setSentDate(new Date());
        // 设置邮件的正文
        minehelper.setText(content,true);
        // 发送邮件
        javaMailSender.send(mimeMessage);

        redisCache.setCacheObject(RedisConstants.EMAIL_CODE+email,code +"");
        redisCache.expire(RedisConstants.EMAIL_CODE+email,RedisConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        logger.info("邮箱验证码发送成功,接收邮箱为：{},验证码为:{}",email,code);
    }

    public void friendPassSendEmail(String email){
        String content = "<html>\n" +
                "<body>\n" +
                "    <p>您在"+"<a href='http://www.shiyit.com'>拾壹博客</a>"+"站点申请友链加入审核通过!!</span>\n" +
                "<p style='padding: 20px;'>感谢您的选择，本站将会竭尽维护好站点稳定，分享高质量的文章，欢迎相互交流互访。</p>" +
                "<p>可前往<a href='http://www.shiyit.com/links'>本站友链</a>查阅您的站点。</p></body>\n" +
                "</html>";
        //创建一个MINE消息
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            minehelper.setSubject("拾壹博客友链审核通知");
            // 设置邮件发送者
            minehelper.setFrom(javaMailSender.getUsername());
            // 设置邮件接收者，可以有多个接收者，中间用逗号隔开
            minehelper.setTo(email);
            // 设置邮件发送日期
            minehelper.setSentDate(new Date());
            // 设置邮件的正文
            minehelper.setText(content,true);
            // 发送邮件
            javaMailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
