package com.xjudge.repository;

import com.xjudge.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<Group , Long> {
}
