package com.xjudge.repository;

import com.xjudge.entity.UserContest;
import com.xjudge.entity.key.UserContestKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContestRepo extends JpaRepository<UserContest, UserContestKey> {
}
