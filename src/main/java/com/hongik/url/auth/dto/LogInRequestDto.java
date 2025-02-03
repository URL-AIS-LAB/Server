package com.hongik.url.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogInRequestDto {

    @NotBlank(message = "아이디를 입력해 주세요")
    @Schema(description = "사용자의 로그인 아이디",example = "id")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Schema(description = "사용자의 로그인 비밀번호",example = "password")
    private String password;
    public UsernamePasswordAuthenticationToken getAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}
