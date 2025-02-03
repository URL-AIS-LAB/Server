package com.hongik.url.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInUserRequestDto {
    String username;
    String password;

    @Builder
    public LogInUserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
