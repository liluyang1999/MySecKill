package com.example.lly.util;

import java.util.Objects;

public class TestUtil {


    public static void main(String[] args) {
        Object joker = 3;
        Class<Object> joker2 = Object.class;
        Object joker3 = joker2;
        System.out.println(joker3 instanceof Class<?>);
        System.out.println(joker3 instanceof Object);

    }


}
