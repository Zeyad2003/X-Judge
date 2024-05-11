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
    Optional<Problem> findByCodeAndOnlineJudge(String code, OnlineJudgeType onlineJudge);
    Page<Problem> findByTitleContaining(String title, Pageable pageable);
    Page<Problem> findByOnlineJudgeContaining(OnlineJudgeType source, Pageable pageable);
    Page<Problem> findByCodeContaining(String problemCode, Pageable pageable);

    @Query(value = "SELECT p FROM Problem p " +
            "WHERE (:source IS NULL OR :source='' OR CAST(p.onlineJudge AS string) LIKE %:source%) " +
            "AND (:problemCode IS NULL OR :problemCode='' OR p.code LIKE %:problemCode%) " +
            "AND (:title IS NULL OR :title='' OR p.title LIKE %:title%) " +
            "AND (:contestName IS NULL OR :contestName='' OR p.contestName LIKE %:contestName%) " +
            "order by p.id DESC ")
    Page<Problem> filterProblems(@Param("source") String source, @Param("problemCode") String problemCode, @Param("title") String title, @Param("contestName") String contestName, Pageable pageable);


}