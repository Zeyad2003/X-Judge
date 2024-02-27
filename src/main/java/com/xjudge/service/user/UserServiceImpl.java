package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public User getUserByHandle(String userHandle) {
        return userRepo.findByHandle(userHandle).orElseThrow(
                () -> new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepo.findUserByEmail(userEmail).orElseThrow(
                () -> new XJudgeException("There's no email {" + userEmail + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
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
