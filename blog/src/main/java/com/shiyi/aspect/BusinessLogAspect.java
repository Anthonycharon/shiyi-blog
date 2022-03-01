package com.shiyi.aspect;

import com.shiyi.annotation.BusinessLog;
import com.shiyi.common.ApiResult;
import com.shiyi.entity.UserLog;
import com.shiyi.mapper.UserLogMapper;
import com.shiyi.utils.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @title: OperationAspect
 * @description: 操作日志切面处理类
 */
@Aspect
@Component
@Slf4j
public class BusinessLogAspect {
    @Autowired
    UserLogMapper sysLogMapper;


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 设置操作日志切入点   在注解的位置切入代码
     */
    @Pointcut("@annotation(businessLog)")
    public void pointcut(BusinessLog businessLog) {
    }

    @Around(value = "pointcut(businessLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, BusinessLog businessLog) throws Throwable {

        //先执行业务
        Object result = joinPoint.proceed();

        try {
            // 日志收集
            handle(joinPoint,(ApiResult) result);

        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        return result;
    }
    /**
     * 记录操作日志
     * @param joinPoint 方法的执行点
     * @param result  方法返回值
     * @throws Throwable
     */
    public void handle(ProceedingJoinPoint  joinPoint, ApiResult result) throws Throwable {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        try {
            UserLog sysLog = new UserLog();
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();
            //获取操作
            BusinessLog annotation = method.getAnnotation(BusinessLog.class);
            if (!annotation.save()) return;
            sysLog.setModel(annotation.value());
            sysLog.setType(annotation.type());
            sysLog.setDescription(annotation.desc());
            //操作时间
            sysLog.setCreateTime(Timestamp.valueOf(sdf.format(new Date())));
            //操作用户
            String ip = IpUtils.getIp(request);
            sysLog.setIp(ip);
            sysLog.setAddress(IpUtils.getCityInfo(ip));
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
            String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
            sysLog.setClientType(clientType); //客户端类型  手机、电脑、平板
            String os = userAgent.getOperatingSystem().getName();
            sysLog.setAccessOs(os);//操作系统类型
            String browser = userAgent.getBrowser().toString();
            sysLog.setBrowser(browser);   // 浏览器类型
            //返回值信息
            sysLog.setResult(result.getMessage());
            //保存日志
            sysLogMapper.insert(sysLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
