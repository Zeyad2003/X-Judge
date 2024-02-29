package com.xjudge.repository;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Long> {
    List<Submission> findAllByContestId(Long contestId);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.problem.problemCode = ?1 AND s.ojType = ?2 AND s.verdict = 'Accepted'")
    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);
}
