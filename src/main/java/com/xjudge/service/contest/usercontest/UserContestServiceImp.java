package com.xjudge.service.contest.usercontest;

import com.xjudge.entity.UserContest;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.repository.UserContestRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserContestServiceImp implements UserContestService{
    private final UserContestRepo userContestRepo;


    @Override
    public UserContest save(UserContest userContest) {
        return userContestRepo.save(userContest);
    }

    @Override
    public boolean existsById(UserContestKey userContestKey) {
        return userContestRepo.existsById(userContestKey);
    }

    @Override
    public UserContest markAsCheater(UserContestKey id) {
        UserContest userContest = userContestRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user contest ID: " + id));
        userContest.setCheater(true);
        return userContestRepo.save(userContest);
    }


}
