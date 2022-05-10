package com.shiyi.enums;

public enum FileUploadWayEnum {

    LOCAL(0,"本地上传"),

    QI_NIU(1,"七牛云上传"),

    ALI(2,"阿里云上传");


    public int code;
    public String desc;

    FileUploadWayEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
