package com.hongik.url.common.response.error.exception;



import com.hongik.url.common.response.error.ErrorCode;
import lombok.Getter;


@Getter
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException(ErrorCode code) {
        super(code.getMessage());
    }
}
