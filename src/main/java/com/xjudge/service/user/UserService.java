package com.xjudge.service.user;

import com.xjudge.entity.User;

import java.util.List;

public interface UserService {
    User getUserByHandle(String userHandle);
    User getUserById(Long userId);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
    List<User> getAllUsers();
}
