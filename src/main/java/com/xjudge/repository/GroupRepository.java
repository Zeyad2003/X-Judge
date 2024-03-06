package com.xjudge.repository;

import com.xjudge.entity.Group;
import com.xjudge.model.enums.GroupVisibility;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByVisibility(GroupVisibility visibility, Pageable pageable);

    Optional<Group> findGroupByName(String groupName);
}
