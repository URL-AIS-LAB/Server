package com.hongik.url.common.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    BAD_REQUEST("잘못된 요청입니다."),
    USER_ALREADY_EXISTS("이미 존재하는 유저입니다."),
    BOOK_MARK_ALREADY_EXISTS("이미 즐겨찾기가 존재합니다."),
    NOT_USER_BOOK_MARK("유저의 즐겨찾기가 아닙니다."),
    ACCESS_TOKEN_NOT_MATCH("엑세스 토큰을 확인해주세요"),
    REFRESH_TOKEN_NOT_MATCH("리프레시 토큰을 확인해주세요"),
    NO_MEMBERSHIP_EXISTS("멤버쉽이 존재하지 않습니다."),
    SOCIAL_LOGIN_CODE("코드로 부터 소셜 토큰을 가져오지 못했습니다"),



    /**
     * 401 Unauthorized
     */
    UNAUTHORIZE("인증에 실패하였습니다."),

    /**
     * 403 Forbidden
     */


    /**
     * 404 Not Found
     */
    NOT_FOUND("존재하지 않는 값입니다."),
    USER_NOT_FOUND("존재하지 않는 유저입니다."),

    IMAGE_NOT_FOUND("존재하지 않는 이미지입니다"),


    /**
     * 405 Method Not Allowed


     /**
     * 409 Conflict
     */


    /**
     * 500 Internal Server Error
     */
    DATA_NOT_READY("데이터가 준비되지 않았습니다"),
    SOCIAL_ACCESS_ERROR("내부 엑세스 토큰으로부터 사용자 정보를 가져오지 못했습니다"),
    SOCIAL_REFRESH_TOKEN_ERROR("리프레시 토큰 기반으로 사용자 정보 연결 해제 실패"),
    SOCIAL_UNLINK_FAIL("리프레시 토큰 기반 연동 해제 실패");





    private final String message;
}