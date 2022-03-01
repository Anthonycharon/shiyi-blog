package com.shiyi.enums;

public enum PublishEnum {

    PUBLISH (1, "发布"),

    NO_PUBLISH (0, "下架");

    //创建构造函数
    PublishEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //定义私有方法，获取枚举值
    private final Integer code;
    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
