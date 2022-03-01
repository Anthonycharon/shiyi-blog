package com.shiyi.aspect;

import com.shiyi.annotation.IgnoreUrl;
import com.shiyi.config.MyProperties;
import com.shiyi.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author blue
 * @description:
 * @date 2021/8/27 16:22
 */
@Component
public class IgnoreUrlAspect implements ApplicationRunner {

    private final String SCANURL = "com.shiyi.controller";

    @Autowired
    private MyProperties myProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 设置扫描路径
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(SCANURL)).setScanners(new MethodAnnotationsScanner()));
        // 扫描指定包内带有@IgnoreUrl注解的所有方法集合
        Set<Method> methods = reflections.getMethodsAnnotatedWith(IgnoreUrl.class);
        if (null != methods && !methods.isEmpty()) {
            for (Method method : methods) {
                String excludeUrl = "";
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (requestMapping != null){
                    excludeUrl = requestMapping.value()[0];
                }else if (getMapping != null){
                    excludeUrl = getMapping.value()[0];
                }else {
                    excludeUrl = postMapping.value()[0];
                }
                String baseUrl = "";
                RequestMapping annotation = method.getDeclaringClass().getAnnotation(RequestMapping.class);
                if (null != annotation){
                    String[] valueArr = annotation.value();
                    if (valueArr.length > 0) {
                        baseUrl = valueArr[0];
                    }
                }
                if (StringUtils.isNotBlank(baseUrl)) {
                    excludeUrl = baseUrl + excludeUrl;
                }
                Constants.ignore.add(excludeUrl);
            }
        }
        List<String> ignoreUrls = myProperties.getAuth().getIgnoreUrls();
        ignoreUrls.addAll(Constants.ignore);
    }
}
