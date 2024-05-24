package com.xjudge.repository;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;
import com.xjudge.entity.key.UserGroupKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupKey> {
    Optional<UserGroup> findByUserAndGroup(User user, Group group);
    boolean existsByUserAndGroup(User user, Group group);
    Optional<UserGroup> findByUserHandleAndGroupId(String userHandle, Long groupId);
    List<UserGroup> findAllByUser(User user);
    UserGroup findByUserIdAndGroupId(Long UserId, Long GroupId);
    Page<UserGroup> findByGroupId(Long groupId, Pageable pageable);
}
