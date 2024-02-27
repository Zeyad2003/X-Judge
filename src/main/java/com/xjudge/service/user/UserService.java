package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.model.user.UserModel;

import java.util.List;

public interface UserService {
    void save(User user);
    UserModel findByHandle(String userHandle);
    UserModel findByEmail(String userEmail);
    UserModel findById(Long userId);
    UserModel updateUser(Long id, UserModel user);
    void deleteUser(Long userId);
    List<UserModel> getAllUsers();
    boolean existsByHandle(String userHandle);
    boolean existsByEmail(String userEmail);
}
