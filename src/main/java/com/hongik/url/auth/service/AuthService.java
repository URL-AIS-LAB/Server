package com.hongik.url.auth.service;

import com.hongik.url.auth.dto.LogInRequestDto;
import com.hongik.url.auth.dto.TokenDto;
import com.hongik.url.auth.dto.TokenReIssueResponseDto;
import com.hongik.url.auth.dto.TokenResponseWithUserUsernameDto;
import com.hongik.url.auth.jwt.TokenProvider;
import com.hongik.url.common.response.error.ErrorCode;
import com.hongik.url.common.response.error.exception.BadRequestException;
import com.hongik.url.common.response.error.exception.NotFoundException;
import com.hongik.url.user.domain.entity.User;
import com.hongik.url.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public TokenResponseWithUserUsernameDto login(LogInRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorCode.WRONG_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.getAuthenticationToken();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        String key = authentication.getName();
        TokenDto token = tokenProvider.createToken(authentication);

        // redis에 refresh-token 저장
        saveLoginProcessAtRedis(key, token);

        return new TokenResponseWithUserUsernameDto(token.getType(),
                token.getAccessToken(), token.getRefreshToken(),
                token.getAccessTokenValidationTime(), user.getUsername());
    }



    @Transactional
    public TokenReIssueResponseDto reIssue(String accessToken, String refreshToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        String key = authentication.getName();
        String storedRefreshToken = redisTemplate.opsForValue().get(key);

        // redis에서 꺼내온 refreshToken과 비교
        if (!storedRefreshToken.equals(refreshToken)) {
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_NOT_MATCH);
        }

        TokenDto token = tokenProvider.createToken(authentication);
        saveLoginProcessAtRedis(authentication.getName(), token);

        return new TokenReIssueResponseDto(token.getType(), token.getAccessToken(), token.getRefreshToken(), token.getAccessTokenValidationTime());
    }

    @Transactional
    public void logout(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new BadRequestException(ErrorCode.ACCESS_TOKEN_NOT_MATCH);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String key = authentication.getName();
        String storedRefreshToken = redisTemplate.opsForValue().get(key);

        if (storedRefreshToken != null) {
            redisTemplate.delete(key);
        }

        Long expiration = tokenProvider.getExpiration(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    private void saveLoginProcessAtRedis(String key, TokenDto token) {
        redisTemplate.opsForValue().set(key, token.getRefreshToken(), token.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
    }
}
