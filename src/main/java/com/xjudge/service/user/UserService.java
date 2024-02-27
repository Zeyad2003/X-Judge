package com.xjudge.service.user;

import com.xjudge.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    User getUserByHandle(String userHandle);
    User getUserByEmail(String userEmail);
    User getUserById(Long userId);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    boolean existsByHandle(String userHandle);
    boolean existsByEmail(String userEmail);
}
