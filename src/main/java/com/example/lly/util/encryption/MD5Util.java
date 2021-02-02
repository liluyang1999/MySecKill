package com.example.lly.util.encryption;

import com.example.lly.util.BaseUtil;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.Arrays;

public class MD5Util {

    //固定盐值，混淆Url链接
    private static final String SALT = "FixedSaltValue";

    public static String encodeString(String inputStr) {
        if(inputStr == null) {
            return null;
        }

        MessageDigest md5Encoder = null;
        try {
            md5Encoder = MessageDigest.getInstance("MD5");  //MD5 or SHAUtil-1
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return null;
        }

        byte[] inputStrBytes = inputStr.getBytes(BaseUtil.defaultCharset);
        byte[] encodedBytes =  md5Encoder.digest(inputStrBytes);

        //把这16个字节表示成32个16进制数字来可视化编码结果
        String[] hexArray = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        StringBuilder result = new StringBuilder();
        for (byte encodedByte : encodedBytes) {   //循环16次
            int low = encodedByte & 0x0f;
            int high = (encodedByte >>> 4 & 0x0f);
            result.append(hexArray[high]);
            result.append(hexArray[low]);
        }
        return result.toString();
    }


    public static String encodeStringBySpring(String inputStr) {
        if(inputStr == null) {
            return null;
        }

        byte[] bytes = inputStr.getBytes(BaseUtil.defaultCharset);
        return DigestUtils.md5DigestAsHex(bytes).toUpperCase();
    }

    //加入盐值混淆密码后使用md5加密
    private static String convertString(String input, String salt) {
        StringBuilder form = new StringBuilder();
        int saltSize = salt.length();
        int inputSize = input.length();
        if(saltSize <= inputSize) {
            for(int i = 0; i < saltSize; i++) {
                form.append(input.charAt(i));
                form.append(salt.charAt(i));
            }
            return form.toString().concat(input.substring(saltSize, inputSize));
        } else {
            for(int i = 0; i < inputSize; i++) {
                form.append(input.charAt(i));
                form.append(salt.charAt(i));
            }
            return encodeString(form.toString().concat(salt.substring(inputSize, saltSize)));
        }

    }

    //二次加密
    public static String secondEncode(String input) {
        char[] inputArray = input.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < input.length(); i++) {
            char each = (char) (inputArray[i] ^ 'l');
            stringBuilder.append(each);
        }
        return stringBuilder.toString();
    }

    public static String secondDecode(String input) {
        char[] inputArray = input.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < inputArray.length; i++) {
            char each = (char) (inputArray[i] ^ 'l');
            stringBuilder.append(each);
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        String password = "123456";
        System.out.println(encodeString(password));
        System.out.println(secondEncode(encodeString(password)));
        System.out.println(secondDecode(secondEncode(encodeString(password))));
    }

}

    //传入随机SALT后再封装一次存入数据库
//    public static String clientToDB(String input, String randomSalt) {
//        String serverPass = clientToServer(input);
//        String dbPass = serverToDB(serverPass, randomSalt);
//        return dbPass;
//    }
//
//    private static String clientToServer(String input) {
//        return convertString(input, SALT);
//    }
//
//    private static String serverToDB(String input, String randomSalt) {
//        return convertString(input, randomSalt);
//    }

