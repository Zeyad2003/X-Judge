package com.xjudge.service.group;

import com.xjudge.entity.Contest;
import com.xjudge.entity.Group;
import com.xjudge.entity.User;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupMemberModel;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface GroupService {

    // CRUD operations
    Page<GroupModel> getAllGroups(Principal connectedUser,Pageable pageable);
    GroupModel getSpecificGroup(Long id);
    GroupModel getSpecificGroupByName(String name);
    GroupModel create(GroupRequest groupRequest, Principal connectedUser);

    GroupModel update(Long groupId, GroupRequest groupRequest);
    void delete(Long groupId);

    // Contest and invitation
    void inviteUser(Long groupId, Long receiverId, Principal connectedUser);

    // Join and leave group
    void join(Long groupId, Principal connectedUser);
    void join(GroupModel group, User user);
    void requestJoin(Long groupId, Principal connectedUser);
    void acceptRequest(Long requestId);
    void declineRequest(Long requestId);
    void leave(Long groupId, Principal connectedUser);

    // Group information
    List<GroupContestModel> getGroupContests(Long groupId);

    Page<GroupMemberModel> getGroupMembers(Long groupId, Pageable pageable);

    List<GroupModel> getGroupsOwnedByUser(Principal connectedUser);

    // Public or private group
    boolean isPublic(Group group);
    boolean isPrivate(Group group);
    void acceptInvitation(Long invitationId, Principal connectedUser);
    void declineInvitation(Long invitationId, Principal connectedUser);
    Page<GroupModel> getGroupsByUserHandle(String handle,Pageable pageable);
}
