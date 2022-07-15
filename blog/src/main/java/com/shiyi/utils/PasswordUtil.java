package com.shiyi.utils;

import cn.dev33.satoken.secure.SaSecureUtil;

/**
 *  <p> 加密工具 </p>
 *
 * @description:
 * @author: blue
 * @date: 2019/10/13 0013 15:25
 */
public class PasswordUtil {

    final static String key = "shiyi2022";
    /**
     * 校验密码是否一致
     *
     * @param newPassword: 前端传过来的密码
     * @param oldPassword：数据库中储存加密过后的密码
     * @return
     */
    public static boolean isValidPassword(String oldPassword, String newPassword) {
        return oldPassword.equalsIgnoreCase(aesEncrypt(newPassword));
    }

    /**
     * AES加密
     *
     * @param password：密码
     * @return
     */
    public static String aesEncrypt(String password){
        return SaSecureUtil.aesEncrypt(key, password);
    }

    public static void main(String[] args) {
        System.out.println(aesEncrypt("123456"));
    }
}
