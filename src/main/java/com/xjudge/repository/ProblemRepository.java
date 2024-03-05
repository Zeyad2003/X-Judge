package com.xjudge.repository;

import com.xjudge.entity.Problem;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCodeAndSource(String problemCode, OnlineJudgeType problemSource);
    Page<Problem> findByTitleContaining(String title, Pageable pageable);
    Page<Problem> findBySourceContaining(OnlineJudgeType source, Pageable pageable);
    Page<Problem> findByProblemCodeContaining(String problemCode, Pageable pageable);
}