package com.xjudge.repository;

import com.xjudge.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Long> {
    List<Submission> findAllByContestId(Long contestId);
}
