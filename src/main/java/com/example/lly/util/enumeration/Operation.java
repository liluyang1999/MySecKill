package com.example.lly.util.enumeration;

public enum Operation {

    SUCCESS(1), FAIL(0);

    Operation(int flag) {
        this.flag = flag;
    }

    public int flag;

}
