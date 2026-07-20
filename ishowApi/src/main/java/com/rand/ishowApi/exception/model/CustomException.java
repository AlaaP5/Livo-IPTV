package com.rand.ishowApi.exception.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {

    private final String code;
    private final Object[] args;

    public CustomException(String code, Object[] args) {
        this.code = code;
        this.args = args;
    }


    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

}