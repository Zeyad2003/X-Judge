package com.xjudge.repository;

import com.xjudge.entity.ContestProblem;
import com.xjudge.entity.key.ContestProblemKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestProblemRepo extends JpaRepository<ContestProblem , ContestProblemKey> {
    @Transactional
    void deleteAllByContestId(Long contestId);

    ContestProblem findByProblemHashtag(String problemHashtag);
}
