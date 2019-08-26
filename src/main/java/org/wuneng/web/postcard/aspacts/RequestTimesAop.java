package org.wuneng.web.postcard.aspacts;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wuneng.web.postcard.annotations.RequestTimes;
import org.wuneng.web.postcard.utils.DateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RequestTimesAop {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //切面范围
    @Pointcut("execution(public * org.wuneng.web.postcard.controllers..*.*(..))")
    public void WebPointCut() {
    }

    @Before("WebPointCut() && @annotation(times)")
    public void ifOvertimes(RequestTimes times) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String key = "times".concat(url).concat(ip);
        long count = stringRedisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            stringRedisTemplate.expire(key, times.time(), TimeUnit.MILLISECONDS);
        }
        if (count > times.count()) {
            System.out.println("until->"+times.count());
            HttpServletResponse response = attributes.getResponse();
            response.setStatus(403);
        }
    }
}
