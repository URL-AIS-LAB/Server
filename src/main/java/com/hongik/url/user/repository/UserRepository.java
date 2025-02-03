package com.hongik.url.user.repository;

import com.hongik.url.user.domain.Role;
import com.hongik.url.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findByName(String name);
    List<User> findByRole(Role role);

    List<User> findAll();

    Boolean existsByUsername(String username);
}
