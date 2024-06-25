package com.xjudge.service.group.userGroupService;

import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.entity.UserGroup;

import java.security.Principal;
import java.util.List;


public interface UserGroupService {
    boolean existsByUserAndGroup(User user, Group group);
    UserGroup findByUserHandleAndGroupId(String userHandle, Long groupId);
    UserGroup findByUserHandleAndGroupIdOrElseNull(String userHandle, Long groupId);
    List<UserGroup> findAllByUserAndRole(User user);
    void save(UserGroup userGroup);
    void delete(UserGroup userGroup);
    String findRoleByUserAndGroupId(Principal connectedUser, Long groupId);
}
