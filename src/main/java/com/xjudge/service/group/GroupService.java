package com.xjudge.service.group;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.model.group.GroupRequest;

import java.security.Principal;
import java.util.List;

public interface GroupService {

    // CRUD operations
    List<Group> publicGroups();
    Group getSpecificGroup(Long id);
    Group create(GroupRequest groupRequest, Principal connectedUser);

    Group update(Long groupId, GroupRequest groupRequest);
    void delete(Long groupId);

    // Contest and invitation
    void addContest(Long contestId, Long groupId);
    void inviteUser(Long groupId, Long receiverId, Principal connectedUser);

    // Join and leave group
    void join(Long groupId, Long userId);
    void join(Group group, User user);
    void leave(Long groupId, Long userId);

    // Group information
    List<Contest> Contests(Long groupId);
    List<User> Users(Long groupId);

    // Public or private group
    boolean isPublic(Group group);
    boolean isPrivate(Group group);
    void acceptInvitation(Long invitationId, Principal connectedUser);
    void declineInvitation(Long invitationId, Principal connectedUser);
}
