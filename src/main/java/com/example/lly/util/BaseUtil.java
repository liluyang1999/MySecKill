package com.example.lly.util;

import com.example.lly.aop.SeckillLock;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class BaseUtil {

    public static final long SERIAL_VERSION_UID = 1L;

    public static final long MIN_LONG = Long.MIN_VALUE;

    public static final long MAX_LONG = Long.MAX_VALUE;

    public static final double MIN_DOUBLE = Double.MIN_VALUE;

    public static final double MAX_DOUBLE = Double.MAX_VALUE;

    public static Charset defaultCharset = StandardCharsets.UTF_8;

    public static final String imagePath = "/static/image";

    public static String addQuotationMark(String str) {
        return "'" + str + "'";
    }

    public static String addPrefix(String str, String prefix) {
        return prefix + str;
    }

    public static String getTableName(Object entity) {
        return "t_" + entity.getClass().getSimpleName().toLowerCase();
    }

    public static String getTableName(Class<?> clazz) {
        return "t_" + clazz.getSimpleName().toLowerCase();
    }

    public static Boolean notNullAndBlank(String str) {
        return (str != null) && (!"".equals(str));
    }

    public static String handlePassword(String input) {
        StringBuffer oldStringBuffer = new StringBuffer(input);
        StringBuffer newStringBuffer = new StringBuffer();
        for (int index = 0; index < oldStringBuffer.length(); index++) {
            char each = oldStringBuffer.charAt(index);
            if (each != '\\') {
                newStringBuffer.append(each);
            } else {
                newStringBuffer.append("\\\\");
            }
        }
        return newStringBuffer.toString();
    }

    public static String addIdToName(String name, Integer id) {
        return name + ":" + id;
    }

    //复制一份
    public static <T> T copyFrom(T src) throws RuntimeException {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        T copy = null;
        try {
            out = new ObjectOutputStream(memoryBuffer);
            out.writeObject(src);
            out.flush();  //输出加缓冲进入memoryBuffer里
            in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
            copy = (T) in.readObject();
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(out != null) {
                try{
                    out.close();
                    out = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(in != null) {
                    try {
                        in.close();
                        in = null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return copy;
    }

    public static LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    public static Timestamp convertToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    public static String generateRandomSalt(int number) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }


    public static String convertDateFormat(Timestamp date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
        return date.toLocalDateTime().format(dateTimeFormatter);
    }


    public static String convertDateFormat(Timestamp date, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return date.toLocalDateTime().format(dateTimeFormatter);
    }

    public static void main(String[] args) {

    }

    @SeckillLock
    public void hello() {
        System.out.println("Hello World~");
    }

}
