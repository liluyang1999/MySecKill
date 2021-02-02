package com.example.lly.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class LocationUtil {

    private static final Logger logger = LoggerFactory.getLogger(LocationUtil.class);

    //获取反向代理IP地址
    public static String getIpAddress() {
        String ip = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes)(Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))).getRequest();
            ip = request.getHeader("x-forwarded-for");
            if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch(Exception e) {
            logger.error("LocationUtil异常", e);
        }

        if(StringUtils.isNoneEmpty(ip) && ip.length() >= 16) {
            int index = ip.indexOf(",");
            if(index > 0) {
                ip = ip.substring(0, index);
            }
        }
        return ip;
    }




}
