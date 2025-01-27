package com.hongik.url.common.response.error.exception;


import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException(ErrorCode code) {
        super(code.getMessage());
    }
}
