package com.example.lly.util.enumeration;

public enum Gender {

    MALE("男性"), FEMAIE("女性");

    // ( ) 括号里的即为参数，必须在枚举成员之后写出构造函数传入参数，个数类型要对应.
    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
