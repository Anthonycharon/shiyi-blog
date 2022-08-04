package com.shiyi.utils;

import com.shiyi.common.RedisConstants;
import com.shiyi.entity.SystemConfig;
import com.shiyi.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
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
        message.setFrom(Objects.requireNonNull(javaMailSender.getUsername()));
        // 设置邮件接收者，可以有多个接收者，中间用逗号隔开
        message.setTo("1248954763@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());
        // 设置邮件的正文
        message.setText(content);
        // 发送邮件
        javaMailSender.send(message);
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
            minehelper.setFrom(Objects.requireNonNull(javaMailSender.getUsername()));
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

    /**
     * 发送邮箱验证码
     */
    public void sendCode(String email) throws MessagingException {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = "<html>\n" +
                "\t<body><div id=\"contentDiv\" onmouseover=\"getTop().stopPropagation(event);\" onclick=\"getTop().preSwapLink(event, 'html', 'ZC0004_vDfNJayMtMUuKGIAzzsWvc8');\" style=\"position:relative;font-size:14px;height:auto;padding:15px 15px 10px 15px;z-index:1;zoom:1;line-height:1.7;\" class=\"body\">\n" +
                "  <div id=\"qm_con_body\">\n" +
                "    <div id=\"mailContentContainer\" class=\"qmbox qm_con_body_content qqmail_webmail_only\" style=\"opacity: 1;\">\n" +
                "      <style type=\"text/css\">\n" +
                "        .qmbox h1,.qmbox \t\t\th2,.qmbox \t\t\th3 {\t\t\t\tcolor: #00785a;\t\t\t}\t\t\t.qmbox p {\t\t\t\tpadding: 0;\t\t\t\tmargin: 0;\t\t\t\tcolor: #333;\t\t\t\tfont-size: 16px;\t\t\t}\t\t\t.qmbox hr {\t\t\t\tbackground-color: #d9d9d9;\t\t\t\tborder: none;\t\t\t\theight: 1px;\t\t\t}\t\t\t.qmbox .eo-link {\t\t\t\tcolor: #0576b9;\t\t\t\ttext-decoration: none;\t\t\t\tcursor: pointer;\t\t\t}\t\t\t.qmbox .eo-link:hover {\t\t\t\tcolor: #3498db;\t\t\t}\t\t\t.qmbox .eo-link:hover {\t\t\t\ttext-decoration: underline;\t\t\t}\t\t\t.qmbox .eo-p-link {\t\t\t\tdisplay: block;\t\t\t\tmargin-top: 20px;\t\t\t\tcolor: #009cff;\t\t\t\ttext-decoration: underline;\t\t\t}\t\t\t.qmbox .p-intro {\t\t\t\tpadding: 30px;\t\t\t}\t\t\t.qmbox .p-code {\t\t\t\tpadding: 0 30px 0 30px;\t\t\t}\t\t\t.qmbox .p-news {\t\t\t\tpadding: 0px 30px 30px 30px;\t\t\t}\n" +
                "      </style>\n" +
                "      <div style=\"max-width:800px;padding-bottom:10px;margin:20px auto 0 auto;\">\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #fff;border-collapse: collapse; border:1px solid #e5e5e5;box-shadow: 0 10px 15px rgba(0, 0, 0, 0.05);text-align: left;width: 100%;font-size: 14px;border-spacing: 0;\">\n" +
                "          <tbody>\n" +
                "            <tr style=\"background-color: #f8f8f8;\">\n" +
                "              <td>\n" +
                "                <img style=\"padding: 15px 0 15px 30px;width:50px\" src=\"http://img.shiyit.com/FjzfvfWYZVED7eXMS4EL8KNR949K\">" +
                "                <span>拾壹博客. </span>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <h1 style=\"font-size: 26px; font-weight: bold;\">验证您的注册邮箱地址</h1>\n" +
                "                <p style=\"line-height:1.75em;\">感谢您注册 拾壹博客. </p>\n" +
                "                <p style=\"line-height:1.75em;\">以下是您的邮箱注册验证码，请将它输入到 拾壹博客 的邮箱验证码输入框中:</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-code\">\n" +
                "                <p style=\"color: #253858;text-align:center;line-height:1.75em;background-color: #f2f2f2;min-width: 200px;margin: 0 auto;font-size: 28px;border-radius: 5px;border: 1px solid #d9d9d9;font-weight: bold;\">434696</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <p style=\"line-height:1.75em;\">这一封邮件包括一些您的私密的 拾壹博客 账号信息，请不要回复或转发它，以免带来不必要的信息泄露风险。 </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "              <td class=\"p-intro\">\n" +
                "                <hr>\n" +
                "                <p style=\"text-align: center;line-height:1.75em;\">shiyi - 拾壹博客</p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </div>\n" +
                "      <style type=\"text/css\">\n" +
                "        .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {display: none !important;}\n" +
                "      </style>\n" +
                "    </div>\n" +
                "  </div><!-- -->\n" +
                "  <style>\n" +
                "    #mailContentContainer .txt {height:auto;}\n" +
                "  </style>\n" +
                "</div></body>\n" +
                "</html>\n";
        //创建一个MINE消息
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper minehelper = new MimeMessageHelper(mimeMessage, true);
        // 设置邮件主题
        minehelper.setSubject("拾壹博客邮箱验证");
        // 设置邮件发送者
        minehelper.setFrom(Objects.requireNonNull(javaMailSender.getUsername()));
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
}
