package com.hongik.url.user.dto.response;

import com.hongik.url.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {
    private String name;
    private String username;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getName(), user.getUsername());
    }
}
