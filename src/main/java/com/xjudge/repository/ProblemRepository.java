package com.xjudge.repository;

import com.xjudge.entity.Problem;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemCodeAndSource(String problemCode, OnlineJudgeType problemSource);
    Page<Problem> findByTitleContaining(String title, Pageable pageable);
    Page<Problem> findBySourceContaining(OnlineJudgeType source, Pageable pageable);
    Page<Problem> findByProblemCodeContaining(String problemCode, Pageable pageable);

    @Query(value = "SELECT p FROM Problem p " +
            "WHERE (:source IS NULL OR CAST(p.source AS string) LIKE %:source%) " +
            "AND (:problemCode IS NULL OR p.problemCode LIKE %:problemCode%) " +
            "AND (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:contestName IS NULL OR p.contestName LIKE %:contestName%) " +
            "order by p.id")
    Page<Problem> filterProblems(@Param("source") String source, @Param("problemCode") String problemCode, @Param("title") String title, @Param("contestName") String contestName, Pageable pageable);

}