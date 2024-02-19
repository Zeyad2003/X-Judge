package com.xjudge.service.user;

import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.UserRepo;
import com.xjudge.service.scraping.codeforces.CodeforcesGetProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;

    @Override
    public User getUserByHandle(String userHandle) {
        Optional<User> user = userRepo.findByHandle(userHandle);
        if(user.isPresent()) return user.get();

        throw new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND);
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()) return user.get();

        throw new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND);
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
}
