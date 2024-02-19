package com.xjudge.repository;

import com.xjudge.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepo extends JpaRepository<Contest, Long> {
}
