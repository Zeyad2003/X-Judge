package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;

import java.util.Optional;

public interface UserGroupService {
    boolean existsByUserAndGroup(User user, Group group);
    Optional<UserGroup> findByUserAndGroup(User user, Group group);
    void save(UserGroup userGroup);
    void deleteById(Long id);
}
