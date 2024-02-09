package com.xjudge.repository;

import com.xjudge.entity.Problem;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCode(String problemCode);

    Optional<Problem> findByProblemCodeAndProblemSource(String problemCode, OnlineJudgeType problemSource);
}