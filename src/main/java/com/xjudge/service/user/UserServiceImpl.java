package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.UserMapper;
import com.xjudge.model.user.UserModel;
import com.xjudge.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findUserByHandle(String userHandle) {
        return userRepo.findByHandle(userHandle).orElseThrow(
                () -> new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelByHandle(String userHandle) {
        return userMapper.toModel(userRepo.findByHandle(userHandle).orElseThrow(
                () -> new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public User findUserByEmail(String userEmail) {
        return userRepo.findUserByEmail(userEmail).orElseThrow(
                () -> new XJudgeException("There's no email {" + userEmail + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelByEmail(String userEmail) {
        return userMapper.toModel(userRepo.findUserByEmail(userEmail).orElseThrow(
                () -> new XJudgeException("There's no email {" + userEmail + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelById(Long userId) {
        return userMapper.toModel(userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public UserModel updateUser(Long id, UserModel user) {
        User oldUser = userRepo.findById(id).orElseThrow(
                () -> new XJudgeException("User not found", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        oldUser.setHandle(user.getHandle());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setSchool(user.getSchool());
        oldUser.setPhotoUrl(user.getPhotoUrl());
        // api must store photo and return url
        return userMapper.toModel(oldUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("User not found", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        userRepo.delete(user);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toModel)
                .toList();
    }

    @Override
    public boolean existsByHandle(String userHandle) {
        return userRepo.existsByHandle(userHandle);
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return userRepo.existsByEmail(userEmail);
    }
}
