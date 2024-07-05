package com.xjudge.repository;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.model.enums.GroupVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findByVisibility(GroupVisibility visibility, Pageable pageable);

    Optional<Group> findGroupByName(String groupName);


    Page<Group> findGroupsByGroupUsersUser(User user, Pageable pageable);


    Page<Group> searchGroupsByNameContainingIgnoreCaseAndVisibility(String name, GroupVisibility groupVisibility, Pageable pageable);
}
