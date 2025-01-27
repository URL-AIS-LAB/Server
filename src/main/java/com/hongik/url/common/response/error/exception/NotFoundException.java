package com.hongik.url.common.response.error.exception;


import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorCode code) {
        super(code.getMessage());
    }
}
