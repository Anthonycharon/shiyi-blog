package com.shiyi.service;

import javax.mail.MessagingException;

public interface EmailService {
    /**
     * 发送注册验证码
     * @param email 邮箱账号
     */
    void sendRegisterCode(String email) throws MessagingException;

    /**
     * 发送一个普通的验证码
     * @param email 邮箱账号
     */
    void sendBindEmailCode(String email) throws MessagingException;

    /**
     * 友链通过通知
     * @param email 邮箱账号
     */
    void friendPassSendEmail(String email);

    /**
     * 邮箱通知我
     * @param subject 邮箱主题
     * @param content 内容
     */
    void emailNoticeMe(String subject,String content);
}
