package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.model.user.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void save(User user);
    UserModel getUserByHandle(String userHandle);
    UserModel getUserByEmail(String userEmail);
    UserModel getUserById(Long userId);
    UserModel saveUser(User user);
    UserModel updateUser(User user);
    void deleteUser(Long userId);
    List<UserModel> getAllUsers();
    boolean existsByHandle(String userHandle);
    boolean existsByEmail(String userEmail);
}
