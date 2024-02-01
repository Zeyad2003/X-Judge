package com.xjudge.service.group;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.model.group.GroupRequest;

import java.util.List;

public interface GroupService {

    // CRUD operations
    List<Group> publicGroups();
    Group create(GroupRequest groupRequest);
    Group details(Long groupId);
    Group update(Long groupId, GroupRequest groupRequest);
    void delete(Long groupId);

    // Contest and invitation
    void addContest(Long contestId, Long groupId);
    void inviteUser(Long groupId, Long userId);

    // Join and leave group
    void join(Long groupId, Long userId);
    void leave(Long groupId, Long userId);

    // Group information
    List<Contest> Contests(Long groupId);
    List<User> Users(Long groupId);

    // Public or private group
    boolean isPublic(Long groupId);
    boolean isPrivate(Long groupId);

}
