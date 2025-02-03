package com.hongik.url.user.service;

import com.hongik.url.common.response.error.ErrorCode;
import com.hongik.url.common.response.error.exception.BadRequestException;
import com.hongik.url.common.response.error.exception.DuplicateException;
import com.hongik.url.common.response.error.exception.NotFoundException;
import com.hongik.url.user.domain.entity.User;
import com.hongik.url.user.dto.request.CreateUserRequestDto;
import com.hongik.url.user.dto.request.LogInUserRequestDto;
import com.hongik.url.user.dto.response.UserResponseDto;
import com.hongik.url.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUser(CreateUserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateException(ErrorCode.USER_ALREADY_EXISTS);
        }

        userRepository.save(
                new User(requestDto.getName(), requestDto.getUsername(), requestDto.getPassword(), requestDto.getRole())
        );
    }

    public UserResponseDto login (LogInUserRequestDto requestDto){
        User user = userRepository.findByUsernameAndPassword(requestDto.getUsername(), requestDto.getPassword())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserResponseDto.of(user);
    }
}
