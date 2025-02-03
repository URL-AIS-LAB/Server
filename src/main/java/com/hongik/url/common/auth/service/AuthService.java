package com.hongik.url.common.auth.service;

import java.util.concurrent.TimeUnit;

import com.hongik.url.common.auth.dto.LogInRequestDto;
import com.hongik.url.common.auth.dto.TokenDto;
import com.hongik.url.common.auth.dto.TokenReIssueResponseDto;
import com.hongik.url.common.auth.dto.TokenResponseWithUserIdDto;
import com.hongik.url.common.auth.jwt.TokenProvider;
import com.hongik.url.common.response.BaseResponse;
import com.hongik.url.common.response.error.ErrorCode;
import com.hongik.url.common.response.error.exception.UnauthorizedException;
import com.hongik.url.user.domain.entity.User;
import com.hongik.url.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String,String> redisTemplate;

    @Transactional
    public TokenResponseWithUserIdDto login(final LogInRequestDto logInRequestDto){

        System.out.println(logInRequestDto.getUsername());

        User findUser  = userRepository.findByUsername(logInRequestDto.getUsername()).orElseThrow(()->new UnauthorizedException(
                ErrorCode.WRONG_USERNAME));

        if(!passwordEncoder.matches(logInRequestDto.getPassword(),findUser.getPassword())){
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken= logInRequestDto.getAuthenticationToken();
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto=tokenProvider.createToken(authentication);

        saveLoginProcessAtRedis(authentication.getName(),tokenDto);

        return new TokenResponseWithUserIdDto(tokenDto.getType(),tokenDto.getAccessToken(),
                tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime(),findUser.getUsername());

    }


    @Transactional
    public TokenReIssueResponseDto reIssue(final String accessToken, final String refreshToken){

        Authentication authentication= tokenProvider.getAuthentication(accessToken);

        if(!redisTemplate.opsForValue().get(authentication.getName()).equals(refreshToken)){
            throw new UnauthorizedException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        TokenDto tokenDto=tokenProvider.createToken(authentication);
        saveLoginProcessAtRedis(authentication.getName(),tokenDto);

        return new TokenReIssueResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),
                tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(final String accessToken){

        if (!tokenProvider.validateToken(accessToken)){
            throw new UnauthorizedException(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisTemplate.opsForValue().get(authentication.getName())!=null){
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken,"logout",expiration, TimeUnit.MILLISECONDS);


    }

    private ResponseCookie makeResponseCookie(String refreshToken,Long refreshTokenValidationTime){
        return  ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(false)//   true 시 자바스크립트에서 쿠키 접근 불가 따라서 XSS 공격 방지
                .secure(true)//true 시 HTTPS 연결을 통해서만 전달 .
                .path("/")
                .maxAge(refreshTokenValidationTime)
                .sameSite("None")
                .build();
    }

    private void saveLoginProcessAtRedis(String key, TokenDto tokenDto){
        redisTemplate.opsForValue().set(key,tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(),TimeUnit.MILLISECONDS);
    }

}

