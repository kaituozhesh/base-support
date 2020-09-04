package com.ktz.sh.base.enums;

/**
 * @ClassName RequestHeaderEnum
 * @Description 请求头数据
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 11:59
 * @Version V1.0.0
 **/
public enum RequestHeaderEnum {
    SIGN("sign"),
    TIMESTAMP("timestamp");


    private String fieldName;

    RequestHeaderEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
