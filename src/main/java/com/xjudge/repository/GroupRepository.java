package com.xjudge.repository;

import com.xjudge.entity.Group;
import com.xjudge.enums.GroupVisibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByGroupVisibility(GroupVisibility visibility);

}
