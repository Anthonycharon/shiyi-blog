package com.shiyi.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  <p> 全局常用变量 </p>
 *
 * @description :
 * @author : by blue
 * @date : 2019/10/12 14:47
 */
public class Constants {

    /**
     * 接口url
     */
    public static Map<String,String> URL_MAPPING_MAP = new HashMap<>();

    /**
     * 自定义注解路径白名单
     */
    public static List<String> ignore = new ArrayList<>();

    /**
     * 密码加密相关
     */
    public static String SALT = "shiyi2022";

    public static final int HASH_ITERATIONS = 1;

    /**
     * 请求头 - token
     */
    public static final String REQUEST_HEADER = "Authorization";

    /**
     * 请求头 - token
     */
    public static final String X_TOKEN = "X-TOKEN";

    /**
     * 请求头类型：
     * application/x-www-form-urlencoded ： form表单格式
     * application/json ： json格式
     */
    public static final String REQUEST_HEADERS_CONTENT_TYPE = "application/json";

    /**
     * 登录者角色
     */
    public static final String ROLE_LOGIN = "role_login";

    /**
     * 用户状态
     */
    public static final int USER_STATUS = 1;

    /**
     * 用户状态
     */
    public static final int ONE = 1;

    /**
     * 用户状态
     */
    public static final int ZERO = 0;

    /**
     * 未知的
     */
    public static final String UNKNOWN = "未知";

    /**
     * 省
     */
    public static final String PROVINCE = "省";

    /**
     * 市
     */
    public static final String CITY = "市";


    public static final String PRE_TAG = "<span style=\"color:red\">";

    public static final String POST_TAG = "</span>";

    public static final String SITE_URL = "http://www.shiyit.com";


    public static final int USER_ROLE_ID = 2;



}
