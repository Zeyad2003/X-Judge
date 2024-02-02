package com.xjudge.repository;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
}
