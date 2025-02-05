package com.hongik.url.common.response.error.exception;



import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(ErrorCode code) {
        super(code.getMessage());
    }
}
