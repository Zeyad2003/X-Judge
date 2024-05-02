package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;

import java.util.List;


public interface UserGroupService {
    boolean existsByUserAndGroup(User user, Group group);
    UserGroup findByUserAndGroup(User user, Group group);
    UserGroup findByUserHandleAndGroupId(String userHandle, Long groupId);
    List<UserGroup> findAllByUserAndRole(User user);
    void save(UserGroup userGroup);
    void delete(UserGroup userGroup);
    void deleteById(Long id);
}
