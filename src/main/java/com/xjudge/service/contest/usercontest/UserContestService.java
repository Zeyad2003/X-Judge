package com.xjudge.service.contest.usercontest;

import com.xjudge.entity.UserContest;
import com.xjudge.entity.key.UserContestKey;

public interface UserContestService {
    UserContest save(UserContest userContest);
    boolean existsById(UserContestKey userContestKey);

    public UserContest markAsCheater(UserContestKey id);
}
