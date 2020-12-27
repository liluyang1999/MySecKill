package com.example.lly.util.encryption;

import com.example.lly.util.BaseUtil;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;

public class MD5Util {

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
        for (byte encodedByte : encodedBytes) {
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


    //For the test
    public static void main(String[] args) {
        System.out.println(MD5Util.encodeString("Hello World"));
        System.out.println(MD5Util.encodeStringBySpring("Hello World").toUpperCase());
    }

}
