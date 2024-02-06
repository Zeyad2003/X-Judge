package com.xjudge.repository;

import com.xjudge.entity.Problem;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCode(String problemCode);
}