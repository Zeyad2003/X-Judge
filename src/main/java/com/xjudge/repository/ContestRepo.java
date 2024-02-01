package com.xjudge.repository;

import com.xjudge.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepo extends JpaRepository<Contest, Long> {
}
