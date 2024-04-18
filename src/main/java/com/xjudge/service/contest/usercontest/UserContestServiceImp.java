package com.xjudge.service.contest.usercontest;

import com.xjudge.entity.UserContest;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.repository.UserContestRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserContestServiceImp implements UserContestService{
    private final UserContestRepo repo;


    @Override
    public UserContest save(UserContest userContest) {
        return repo.save(userContest);
    }

    @Override
    public boolean existsById(UserContestKey userContestKey) {
        return repo.existsById(userContestKey);
    }
}
