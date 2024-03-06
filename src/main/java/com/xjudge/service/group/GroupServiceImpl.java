package com.xjudge.service.group;

import com.xjudge.entity.*;
import com.xjudge.entity.key.UserGroupKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.GroupMapper;
import com.xjudge.mapper.UserMapper;
import com.xjudge.model.enums.GroupVisibility;
import com.xjudge.model.enums.InvitationStatus;
import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.repository.GroupRepository;
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
    private final UserMapper userMapper;
    private final JoinRequestService joinRequestService;

    @Override
    public Page<GroupModel> getAllGroups(Pageable pageable) {
        Page<Group> groups = groupRepository.findAll(pageable);
        return groups.map(group -> GroupModel.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .creationDate(group.getCreationDate())
                .visibility(group.getVisibility())
                .build());
    }

    @Override
    public GroupModel getSpecificGroup(Long id) {
        return groupMapper.toModel(
                groupRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public GroupModel getSpecificGroupByName(String name) {
        return  groupMapper.toModel(
                groupRepository.findGroupByName(name).orElseThrow(
                        () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
                ));
    }


    @Override
    @Transactional
    public GroupModel create(GroupRequest groupRequest, Principal connectedUser) {

        User leader = userMapper.toEntity(userService.findByHandle(connectedUser.getName()));
        Group group = groupRepository.save(Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .visibility(groupRequest.getVisibility())
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
                        () -> { throw new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND); });
    }

    @Override
    public void addContest(Long contestId, Long groupId) {
        // TODO
//        Optional<Contest> optionalContest = contestRepository.findById(contestId);
//
//        Optional<Group> optionalGroup = groupRepository.findById(groupId);
//
//        if (optionalContest.isPresent() && optionalGroup.isPresent()) {
//            Contest contest = optionalContest.get();
//            Group group = optionalGroup.get();
//            group.addContest(contest);
//            groupRepository.save(group);
//        } else {
//            throw new EntityNotFoundException("Contest or Group not found");
//        }
    }

    @Override
    public void inviteUser(Long groupId, Long receiverId, Principal connectedUser) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );

        User receiver = userMapper.toEntity(userService.findById(receiverId));
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
        User user = userMapper.toEntity(userService.findByHandle(connectedUser.getName()));
        if (isPrivate(group)) {
            throw new XJudgeException("Group is private", GroupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new XJudgeException("User is already in the group", GroupServiceImpl.class.getName(), HttpStatus.ALREADY_REPORTED);
        }
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
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
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
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
        Group group = groupMapper.toEntity(getSpecificGroup(groupId));
        User user = userMapper.toEntity(userService.findByHandle(connectedUser.getName()));
        UserGroup userGroup = userGroupService.findByUserAndGroup(user, group);
        // updated
        userGroupService.delete(userGroup);
    }

    @Override
    public List<Contest> Contests(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ).getGroupContests();
    }

    @Override
    public List<User> Users(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> group.getGroupUsers().stream()
                        .map(UserGroup::getUser)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new XJudgeException("Group not found", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND));
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
        User user = userMapper.toEntity(userService.findByHandle(connectedUser.getName()));
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
        User user = userMapper.toEntity(userService.findByHandle(connectedUser.getName()));
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
