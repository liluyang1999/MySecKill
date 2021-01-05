package com.example.lly.util;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class BaseUtil {

    public static Charset defaultCharset = StandardCharsets.UTF_8;

    public static final long serialVersionUID = 1L;

    public static String addPrefix(String str, String prefix) {
        return str + prefix;
    }

    public static int joker = 3;


}
