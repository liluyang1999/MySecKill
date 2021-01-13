package com.example.lly.util;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class BaseUtil implements Serializable {

    public static final long SERIAL_VERSION_UID = 1L;

    public static final long MIN_LONG = Long.MIN_VALUE;

    public static final long MAX_LONG = Long.MAX_VALUE;

    public static final double MIN_DOUBLE = Double.MIN_VALUE;

    public static final double MAX_DOUBLE = Double.MAX_VALUE;

    public static Charset defaultCharset = StandardCharsets.UTF_8;

    public static String mergeStrings(String  str1, String str2) {
         return str1 + str2;
    }

    public String addPrefix(String str, String prefix) {
        return prefix + str;
    }



}
