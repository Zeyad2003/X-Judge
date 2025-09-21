package com.xjudge.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xjudge.entity.problem.Problem;
import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemPageModel;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

        Optional<Problem> findByCodeAndOnlineJudge(String code, OnlineJudgeType onlineJudge);

        Boolean existsByCodeAndOnlineJudge(String code, OnlineJudgeType onlineJudge);

        @Query("SELECT p.fetchingStatus FROM Problem p WHERE p.code = :code AND p.onlineJudge = :onlineJudge")
        Optional<FetchingStatus> getFetchingStatus(String code, OnlineJudgeType onlineJudge);

        @Query("SELECT p.lastModifiedDate FROM Problem p WHERE p.code = :code AND p.onlineJudge = :onlineJudge")
        Optional<Instant> getLastModifiedDate(String code, OnlineJudgeType onlineJudge);

        @Modifying
        @Query("UPDATE Problem p SET p.fetchingStatus = :status WHERE p.code = :code AND p.onlineJudge = :onlineJudge")
        void updateFetchingStatus(String code, OnlineJudgeType onlineJudge, FetchingStatus status);

        @Query("SELECT new com.xjudge.model.problem.ProblemPageModel(" +
                        "p.onlineJudge, p.code, p.title, p.problemUrl, p.contestName, p.contestUrl, p.solvedCount, p.lastModifiedDate) "
                        +
                        "FROM Problem p")
        Page<ProblemPageModel> findAllForPageModel(Pageable pageable);

        @Query("SELECT new com.xjudge.model.problem.ProblemPageModel(" +
                        "p.onlineJudge, p.code, p.title, p.problemUrl, p.contestName, p.contestUrl, p.solvedCount, p.lastModifiedDate) "
                        +
                        "FROM Problem p " +
                        "WHERE (:onlineJudge IS NULL OR p.onlineJudge = :onlineJudge) " +
                        "AND (:code = '' OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%'))) " +
                        "AND (:title = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                        "AND (:contestName = '' OR LOWER(p.contestName) LIKE LOWER(CONCAT('%', :contestName, '%')))")
        Page<ProblemPageModel> findProblemsByFiltration(OnlineJudgeType onlineJudge, String code,
                        String title,
                        String contestName, Pageable pageable);
}
