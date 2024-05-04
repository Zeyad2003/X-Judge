package com.xjudge.repository;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Long> {
    List<Submission> findAllByContestId(Long contestId);

    List<Submission> findSubmissionsByContestIdAndUserId(Long contestId , Long userId);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.problem.problemCode = ?1 AND s.ojType = ?2 AND s.verdict = 'Accepted'")
    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);

    @Query(value = "SELECT s FROM Submission s " +
            "JOIN s.user u " +
            "JOIN s.problem p " +
            "WHERE (:userHandle IS NULL OR :userHandle='' OR u.handle LIKE %:userHandle%) " +
            "AND (:oj IS NULL OR :oj='' OR CAST(p.source AS string) LIKE %:oj%) " +
            "AND (:problemCode IS NULL OR :problemCode='' OR p.problemCode LIKE %:problemCode%) " +
            "AND (:language IS NULL OR :language='' OR s.language LIKE %:language%) " +
            "order by s.submitTime DESC")
    Page<Submission> filterSubmissions(@Param("userHandle") String userHandle, @Param("oj") String oj, @Param("problemCode") String problemCode, @Param("language") String language, Pageable pageable);
}
