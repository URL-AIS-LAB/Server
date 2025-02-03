package com.hongik.url.common.auth.jwt;

import java.util.Collections;

import com.hongik.url.common.auth.service.CustomUserDetails;
import com.hongik.url.common.response.error.ErrorCode;
import com.hongik.url.common.response.error.exception.UnauthorizedException;
import com.hongik.url.user.domain.entity.User;
import com.hongik.url.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;



    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::getUserDetails)
                .orElseThrow(()->new UnauthorizedException(ErrorCode.WRONG_USERNAME));
    }

    public CustomUserDetails getUserDetails(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("임시");

        return new CustomUserDetails(user.getUsername(), user.getPassword(), Collections.singleton(authority),user);
    }
}