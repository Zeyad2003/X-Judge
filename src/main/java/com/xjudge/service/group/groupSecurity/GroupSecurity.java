package com.xjudge.service.group.groupSecurity;

import com.xjudge.entity.UserGroup;
import com.xjudge.service.group.GroupService;
import com.xjudge.service.group.userGroupService.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupSecurity {

    private final UserGroupService userGroupService;
    private final GroupService groupService;
    public boolean hasAnyRole(String handle, Long groupId, List<String> roles) {
        UserGroup userGroup = userGroupService.findByUserHandleAndGroupId(handle, groupId);
        return roles.stream().anyMatch(role -> userGroup.getRole().name().equals(role));
    }

    public boolean hasRole(String handle, Long groupId, String role) {
        UserGroup userGroup = userGroupService.findByUserHandleAndGroupId(handle, groupId);
        return userGroup.getRole().name().equals(role);
    }

    public boolean isMember(String handle, Long groupId) {
        UserGroup userGroup = userGroupService.findByUserHandleAndGroupId(handle, groupId);
        return userGroup != null;
    }

    public boolean isPublic(Long groupId) {
        return groupService.getSpecificGroup(groupId).getVisibility().name().equals("PUBLIC");
    }
}
