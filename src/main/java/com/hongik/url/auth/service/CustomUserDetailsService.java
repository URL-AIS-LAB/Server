package com.hongik.url.auth.service;

import com.hongik.url.auth.CustomUserDetails;
import com.hongik.url.common.response.error.ErrorCode;
import com.hongik.url.common.response.error.exception.NotFoundException;
import com.hongik.url.user.domain.entity.User;
import com.hongik.url.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::getUserDetails)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public CustomUserDetails getUserDetails(User user) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole().getAuthority());

        return new CustomUserDetails(user.getUsername(), user.getPassword(), Collections.singleton(simpleGrantedAuthority), user);
    }
}
