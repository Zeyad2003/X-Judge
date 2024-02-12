package com.xjudge.repository;

import com.xjudge.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepo extends JpaRepository<Submission, Long> {
}
