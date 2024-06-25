package com.xjudge.service.group;

import com.xjudge.entity.*;
import com.xjudge.entity.key.UserGroupKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.GroupMapper;
import com.xjudge.mapper.UserGroupMapper;
import com.xjudge.model.enums.GroupVisibility;
import com.xjudge.model.enums.InvitationStatus;
import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupMemberModel;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.repository.GroupRepository;
import com.xjudge.repository.UserGroupRepository;
import com.xjudge.service.group.joinRequest.JoinRequestService;
import com.xjudge.service.group.userGroupService.UserGroupService;
import com.xjudge.service.invitiation.InvitationService;
import com.xjudge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final InvitationService invitationService;
    private final UserGroupService userGroupService;
    private final GroupMapper groupMapper;
    private final JoinRequestService joinRequestService;
    private final UserGroupMapper userGroupMapper;
    private final UserGroupRepository userGroupRepository;

    @Override
    public Page<GroupModel> getAllGroups(Principal connectedUser,Pageable pageable) {
        Page<Group> groups = groupRepository.findAll(pageable);
        return groups.map(group -> GroupModel.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .creationDate(group.getCreationDate())
                .visibility(group.getVisibility())
                .leaderHandle(group.getLeaderHandle())
                .userGroupRole(userGroupService.findRoleByUserAndGroupId(connectedUser,group.getId()))
                .members(group.getGroupUsers().size())
                .build());
    }

    @Override
    public Page<GroupModel> getGroupsByUserHandle(String handle, Pageable pageable) {
        User user = userService.findUserByHandle(handle);
        Page<Group> groups = groupRepository.findGroupsByGroupUsersUser(user, pageable);
        return groups.map(group -> GroupModel.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .creationDate(group.getCreationDate())
                .visibility(group.getVisibility())
                .leaderHandle(group.getLeaderHandle())
                .members(group.getGroupUsers().size())
                .build());
    }


    @Override
    public GroupModel getSpecificGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        return groupMapper.toModel(group, group.getGroupUsers().size());
    }

    @Override
    public GroupModel getGroupById(Long id, Principal connectedUser) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        UserGroup userGroup = userGroupService.findByUserHandleAndGroupIdOrElseNull(connectedUser.getName(), id);
        boolean isMember = userGroup != null;
        boolean isLeader = isMember && userGroup.getRole() == UserGroupRole.LEADER;

        return groupMapper.toModel(group, group.getGroupUsers().size(), isMember, isLeader, connectedUser.getName());
    }


    @Override
    @Transactional
    public GroupModel create(GroupRequest groupRequest, Principal connectedUser) {

        User leader = userService.findUserByHandle(connectedUser.getName());
        Group group = groupRepository.save(Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .visibility(groupRequest.getVisibility())
                .leaderHandle(connectedUser.getName())
                .creationDate(LocalDate.now())
                .build());

        UserGroupKey userGroupKey = new UserGroupKey(leader.getId(), group.getId());

        userGroupService.save(UserGroup.builder()
                .id(userGroupKey)
                .user(leader)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.LEADER)
                .build());
        return groupMapper.toModel(group);
    }


    @Override
    public GroupModel update(Long groupId, GroupRequest groupRequest) {
        return groupMapper.toModel(
                groupRepository.findById(groupId)
                        .map(group -> {
                            group.setName(groupRequest.getName());
                            group.setDescription(groupRequest.getDescription());
                            group.setVisibility(groupRequest.getVisibility());
                            return groupRepository.save(group);
                        }).orElseThrow(
                                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
                        ));
    }

    @Override
    public void delete(Long groupId) {
        groupRepository.findById(groupId)
                .ifPresentOrElse(groupRepository::delete,
                        () -> {
                            throw new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND);
                        });
    }

    @Override
    public void inviteUser(Long groupId, String receiverHandle, Principal connectedUser) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );

        User receiver = userService.findUserByHandle(receiverHandle);
        User sender = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (sender.getId().equals(receiver.getId())) {
            throw new XJudgeException("User cannot invite himself", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }

        invitationService.save(Invitation.builder()
                .receiver(receiver)
                .sender(sender)
                .group(group)
                .date(LocalDate.now())
                .status(InvitationStatus.PENDING)
                .build());
    }

    @Override
    public void join(Long groupId, Principal connectedUser) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        User user = userService.findUserByHandle(connectedUser.getName());
        if (isPrivate(group)) {
            throw new XJudgeException("Group is private", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new XJudgeException("User is already in the group", GroupServiceImpl.class.getName(), HttpStatus.ALREADY_REPORTED);
        }
        UserGroupKey userGroupKey = new UserGroupKey(user.getId(), groupId);

        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .id(userGroupKey)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.MEMBER).build());
    }

    @Override
    public void join(GroupModel groupModel, User user) {
        // Check if the user is not already in the group
        Group group = groupMapper.toEntity(groupModel);
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new XJudgeException("User is already in the group", GroupServiceImpl.class.getName(), HttpStatus.ALREADY_REPORTED);
        }
        UserGroupKey userGroupKey = new UserGroupKey(user.getId(), group.getId());

        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .id(userGroupKey)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.MEMBER).build());
    }

    @Override
    public void requestJoin(Long groupId, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        JoinRequest joinRequest = JoinRequest.builder()
                .group(group)
                .user(user)
                .status(InvitationStatus.PENDING)
                .date(LocalDate.now())
                .build();
        this.joinRequestService.save(joinRequest);
    }

    @Override
    public void acceptRequest(Long requestId) {
        // TODO check if user is the leader of the group in controller level before calling this method
        JoinRequest joinRequest = joinRequestService.findById(requestId);
        if (joinRequest.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invalid request", GroupServiceImpl.class.getName(), HttpStatus.BAD_REQUEST);
        }
        joinRequest.setStatus(InvitationStatus.ACCEPTED);
        joinRequestService.save(joinRequest);
    }

    @Override
    public void declineRequest(Long requestId) {
        // TODO check if user is the leader of the group in controller level before calling this method
        JoinRequest joinRequest = joinRequestService.findById(requestId);
        if (joinRequest.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invalid request", GroupServiceImpl.class.getName(), HttpStatus.BAD_REQUEST);
        }
        joinRequest.setStatus(InvitationStatus.DECLINED);
        joinRequestService.save(joinRequest);
    }

    @Override
    public void leave(Long groupId, Principal connectedUser) {
        UserGroup userGroup = userGroupService.findByUserHandleAndGroupId(connectedUser.getName(), groupId);
        // updated
        userGroupService.delete(userGroup);
        if (userGroup.getRole() == UserGroupRole.LEADER) {
            delete(groupId);
        }
    }

    @Override
    public List<GroupContestModel> getGroupContests(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND))
                .getGroupContests()
                .stream()
                .map(groupMapper::toGroupContestModel)
                .toList();
    }

    @Override
    public Page<GroupMemberModel> getGroupMembers(Long groupId, Pageable pageable) {
        return userGroupRepository.findByGroupId(groupId, pageable)
                .map(userGroupMapper::toGroupMemberModel);
    }

    @Override
    public List<GroupModel> getGroupsOwnedByUser(Principal connectedUser) {
        User user = userService.findUserByHandle(connectedUser.getName());
        return userGroupService.findAllByUserAndRole(user).stream()
                .map(userGroup -> groupMapper.toModel(userGroup.getGroup()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPublic(Group group) {
        return group.getVisibility() == GroupVisibility.PUBLIC;
    }

    @Override
    public boolean isPrivate(Group group) {
        return !isPublic(group);
    }

    @Override
    @Transactional
    public void acceptInvitation(Long invitationId, Principal connectedUser) {
        Invitation invitation = invitationService.findById(invitationId);
        User user = userService.findUserByHandle(connectedUser.getName());
        if (!invitation.getReceiver().equals(user)) {
            throw new XJudgeException("User is not the receiver of the invitation", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invitation is not pending", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationService.save(invitation);
        join(groupMapper.toModel(invitation.getGroup()), user);
    }

    @Override
    public void declineInvitation(Long invitationId, Principal connectedUser) {
        Invitation invitation = invitationService.findById(invitationId);
        User user = userService.findUserByHandle(connectedUser.getName());
        if (!invitation.getReceiver().equals(user)) {
            throw new XJudgeException("User is not the receiver of the invitation", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invitation is not pending", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        invitation.setStatus(InvitationStatus.DECLINED);
        invitationService.save(invitation);
    }


}
