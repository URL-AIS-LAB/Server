package com.hongik.url.common.response.error.exception;




import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class BadRequestException extends RuntimeException {

    public BadRequestException(ErrorCode code) {
        super(code.getMessage());
    }
}
