package com.hongik.url.user.dto.request;

import com.hongik.url.user.domain.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateUserRequestDto {
    private String name;
    private String username;
    private String password;
    private Role role;

    @Builder
    public CreateUserRequestDto(String name, String username, String password, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
