package com.xjudge.repository;

import com.xjudge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User , Long> {
    Optional<User> findUserByHandle(String userHandle);
}
