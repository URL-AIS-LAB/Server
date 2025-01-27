package com.hongik.url.common.response.error.exception;



import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(ErrorCode code) {
        super(code.getMessage());
    }
}
