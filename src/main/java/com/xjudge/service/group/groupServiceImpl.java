package com.xjudge.service.group;

import com.xjudge.entity.*;
import com.xjudge.enums.GroupVisibility;
import com.xjudge.enums.InvitationStatus;
import com.xjudge.enums.UserRole;
import com.xjudge.exception.SubmitException;
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
        return groupRepository.findByGroupVisibility(GroupVisibility.PUBLIC);
    }

    @Override
    public Group getSpecificGroup(Long id) {
        return groupRepository.findById(id).orElseThrow(
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
    }


    @Override
    @Transactional
    public Group create(GroupRequest groupRequest, Principal connectedUser) {
        User leader = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Group group = groupRepository.save(Group.builder()
                .groupName(groupRequest.getName())
                .groupDescription(groupRequest.getDescription())
                .groupVisibility(groupRequest.getVisibility())
                .build());
        userGroupService.save(UserGroup.builder()
                .user(leader)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserRole.LEADER)
                .build());
        return group;
    }


    @Override
    public Group update(Long groupId, GroupRequest groupRequest) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    group.setGroupName(groupRequest.getName());
                    group.setGroupDescription(groupRequest.getDescription());
                    group.setGroupVisibility(groupRequest.getVisibility());
                    return groupRepository.save(group);
                }).orElseThrow(
                        () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
                );
    }

    @Override
    public void delete(Long groupId) {
        groupRepository.findById(groupId)
                .ifPresentOrElse(groupRepository::delete,
                        () -> { throw new SubmitException("Group not found", HttpStatus.NOT_FOUND); });
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
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
        User receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new SubmitException("Receiver not found", HttpStatus.NOT_FOUND)
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
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (isPrivate(group)) {
            throw new SubmitException("Group is private", HttpStatus.FORBIDDEN);
        }
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new SubmitException("User is already in the group", HttpStatus.ALREADY_REPORTED);
        }
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserRole.MEMBER).build());
    }

    @Override
    public void join(Group group, User user) {
        // Check if the user is not already in the group
        if (userGroupService.existsByUserAndGroup(user, group)) {
            throw new SubmitException("User is already in the group", HttpStatus.ALREADY_REPORTED);
        }
        userGroupService.save(UserGroup.builder()
                .user(user)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserRole.MEMBER).build());
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
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        ).getGroupContests();
    }

    @Override
    public List<User> Users(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> group.getUsers().stream()
                        .map(UserGroup::getUser)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new SubmitException("Group not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean isPublic(Group group) {
        return group.getGroupVisibility() == GroupVisibility.PUBLIC;
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
            throw new SubmitException("User is not the receiver of the invitation", HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new SubmitException("Invitation is not pending", HttpStatus.FORBIDDEN);
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
            throw new SubmitException("User is not the receiver of the invitation", HttpStatus.FORBIDDEN);
        }
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new SubmitException("Invitation is not pending", HttpStatus.FORBIDDEN);
        }
        invitation.setStatus(InvitationStatus.DECLINED);
        invitationService.save(invitation);
    }
}
