package com.xjudge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xjudge.entity.problem.Problem;
import com.xjudge.model.enums.OnlineJudgeType;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findByCodeAndOnlineJudge(String code, OnlineJudgeType onlineJudge);
}
