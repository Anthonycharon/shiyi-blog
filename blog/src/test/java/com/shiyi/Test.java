package com.shiyi;

import com.shiyi.mapper.ArticleMapper;
import com.shiyi.utils.EmailUtil;
import com.shiyi.utils.RedisCache;
import org.jasypt.encryption.StringEncryptor;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Email;

/**
 * @author blue
 * @date 2022/1/18
 * @apiNote
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test {
    @Autowired
    StringEncryptor encryptor;
    @Autowired
    RedisCache redisCache;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    EmailUtil emailUtil;

    @org.junit.Test
    public void test(){
        String url = encryptor.encrypt("jdbc:mysql://xxxx:3306/blog?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&tinyInt1isBit=false");
        System.out.println(url);
    }

    @org.junit.Test
    public void seo(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");
        String url = "http://www.shiyit.com";
        HttpEntity<String> entity = new HttpEntity<>(url, headers);
        String result = restTemplate.postForObject("http://data.zz.baidu.com/urls?site=www.shiyit.com&token=aw5iVpNEB9aQJOYZ", entity, String.class);
        System.out.println(result);
    }

    @org.junit.Test
    public void send(){
        emailUtil.friendPassSendEmail("1248954763@qq.com");
    }
}
