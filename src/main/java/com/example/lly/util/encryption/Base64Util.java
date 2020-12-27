package com.example.lly.util.encryption;

import com.example.lly.util.BaseUtil;

import java.util.Base64;

public class Base64Util {

    public static String encodeString(String inputStr) {
        if(inputStr == null) {
            return null;
        }

        if(inputStr.length() == 0) {
            return inputStr;
        }

        byte[] bytes = inputStr.getBytes(BaseUtil.defaultCharset);

        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decodeString(String outputStr) {
        if(outputStr == null) {
            return null;
        }

        if(outputStr.length() == 0) {
            return outputStr;
        }

        byte[] bytes = outputStr.getBytes(BaseUtil.defaultCharset);
        byte[] result = Base64.getDecoder().decode(bytes);

        return new String(result, BaseUtil.defaultCharset);
    }


    public static void main(String[] args) {
        System.out.println(encodeString("Hello World"));
        System.out.println(decodeString(encodeString("Hello World")));
    }


}
