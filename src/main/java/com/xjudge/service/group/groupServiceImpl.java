package com.xjudge.service.group;

import com.xjudge.entity.*;
import com.xjudge.exception.XJudgeException;
import com.xjudge.model.enums.GroupVisibility;
import com.xjudge.model.enums.InvitationStatus;
import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.repository.GroupRepository;
import com.xjudge.repository.UserRepo;
import com.xjudge.service.group.userGroupService.UserGroupService;
import com.xjudge.service.invitiation.InvitationService;
import lombok.RequiredArgsConstructor;
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
public class groupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepo userRepository; // Don't user repository, use user service layer
    private final InvitationService invitationService;
    private final UserGroupService userGroupService;

    @Override
    public List<Group> publicGroups() {
        return groupRepository.findByVisibility(GroupVisibility.PUBLIC);
    }

    @Override
    public Group getSpecificGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }


    @Override
    @Transactional
    public Group create(GroupRequest groupRequest, Principal connectedUser) {
        User leader = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Group group = groupRepository.save(Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .visibility(groupRequest.getVisibility())
                .build());
        userGroupService.save(UserGroup.builder()
                .user(leader)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.LEADER)
                .build());
        return group;
    }


    @Override
    public Group update(Long groupId, GroupRequest groupRequest) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    group.setName(groupRequest.getName());
                    group.setDescription(groupRequest.getDescription());
                    group.setVisibility(groupRequest.getVisibility());
                    return groupRepository.save(group);
                }).orElseThrow(
                        () -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
                );
    }

    @Override
    public void delete(Long groupId) {
        groupRepository.findById(groupId)
                .ifPresentOrElse(groupRepository::delete,
                        () -> { throw new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND); });
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
                () -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        User receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new XJudgeException("Receiver not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        User sender = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
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
                () -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (isPrivate(group)) {
            throw new XJudgeException("Group is private", groupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new XJudgeException("User is already in the group", groupServiceImpl.class.getName(), HttpStatus.ALREADY_REPORTED);
        }
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.MEMBER).build());
    }

    @Override
    public void join(Group group, User user) {
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new XJudgeException("User is already in the group", groupServiceImpl.class.getName(), HttpStatus.ALREADY_REPORTED);
        }
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserGroupRole.MEMBER).build());
    }

    @Override
    public void leave(Long groupId, Principal connectedUser) {
        Group group = getSpecificGroup(groupId);
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        UserGroup userGroup = userGroupService.findByUserAndGroup(user, group);
        userGroupService.deleteById(userGroup.getId());
    }

    @Override
    public List<Contest> Contests(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ).getGroupContests();
    }

    @Override
    public List<User> Users(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> group.getGroupUsers().stream()
                        .map(UserGroup::getUser)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new XJudgeException("Group not found", groupServiceImpl.class.getName(), HttpStatus.NOT_FOUND));
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
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!invitation.getReceiver().equals(user)) {
            throw new XJudgeException("User is not the receiver of the invitation", groupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invitation is not pending", groupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationService.save(invitation);
        join(invitation.getGroup(), user);
    }

    @Override
    public void declineInvitation(Long invitationId, Principal connectedUser) {
        Invitation invitation = invitationService.findById(invitationId);
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!invitation.getReceiver().equals(user)) {
            throw new XJudgeException("User is not the receiver of the invitation", groupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new XJudgeException("Invitation is not pending", groupServiceImpl.class.getName(), HttpStatus.FORBIDDEN);
        }
        invitation.setStatus(InvitationStatus.DECLINED);
        invitationService.save(invitation);
    }
}
