package com.example.lly.util.result;

public enum ResponseEnum {

    SUCCESS(200, "操作成功"),
    FAILED(1000, "操作失败"),
    USERNAME_OR_PASSWORD_ERROR(1001, "账号或密码错误！"),
    EMPTY_USERNAME(1002, "账号为空"),
    EMPTY_PASSWORD(1003, "密码为空"),
    TOKEN_EXPIRED(1004, "Token令牌失效"),
    TOKEN_EMPTY(1005, "Token令牌为空"),
    HAS_USERNAME(1006, "该账号已被注册"),
    NOT_LOGIN(1007, "用户没有登录"),
    VERCODE_ERROR(1008, "验证码错误"),
    UNMATCHED_USERNAME(1009, "账号不匹配"),
    INSERT_ERROR(1010, "插入数据失败"),
    SERVER_ERROR(999, "服务器内部错误！");

    private final Integer code;
    private final String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

}

//    public static final int unknownException = 100;
//
//    public static final int SUCCESS = 200;
//
//    public static final int requestError = 400;
//
//    public static final int resourceNotFound = 404;
//
//    public static final int notAllowed = 403;
//
//    public static final int contentTypeError = 406;
//
//    public static final int serverError = 500;