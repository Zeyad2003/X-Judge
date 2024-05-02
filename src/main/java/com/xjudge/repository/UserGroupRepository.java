package com.xjudge.repository;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByUserAndGroup(User user, Group group);
    boolean existsByUserAndGroup(User user, Group group);
    Optional<UserGroup> findByUserHandleAndGroupId(String userHandle, Long groupId);
    List<UserGroup> findAllByUser(User user);

}
