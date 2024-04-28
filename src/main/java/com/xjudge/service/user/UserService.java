package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.model.user.UserModel;

import java.util.List;

public interface UserService {
    User save(User user);
    User findUserByHandle(String userHandle);
    UserModel findUserModelByHandle(String userHandle);
    User findUserByEmail(String userEmail);
    UserModel findUserModelByEmail(String userEmail);
    User findUserById(Long userId);
    UserModel findUserModelById(Long userId);
    UserModel updateUser(Long id, UserModel user);
    void deleteUser(Long userId);
    List<UserModel> getAllUsers();
    boolean existsByHandle(String userHandle);
    boolean existsByEmail(String userEmail);
}
