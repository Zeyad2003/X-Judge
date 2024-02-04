package com.xjudge.service.group;

import com.xjudge.config.security.JwtService;
import com.xjudge.entity.*;
import com.xjudge.enums.GroupVisibility;
import com.xjudge.enums.InvitationStatus;
import com.xjudge.enums.UserRole;
import com.xjudge.exception.SubmitException;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.repository.GroupRepository;
import com.xjudge.repository.InvitationRepository;
import com.xjudge.repository.UserGroupRepository;
import com.xjudge.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class groupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepo userRepository; // Don't user repository, use user service layer
    private final InvitationRepository invitationRepository; // Don't invitation repository, use invitation service layer
    private final UserGroupRepository userGroupRepository;
    private final JwtService jwtService; // ✅

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
    public Group create(GroupRequest groupRequest) {
        return groupRepository.save(Group.builder()
                .groupName(groupRequest.getName())
                .groupDescription(groupRequest.getDescription())
                .groupVisibility(groupRequest.getVisibility())
                .build());
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
                        () -> { throw new SubmitException("Group with ID " + groupId + " not found", HttpStatus.NOT_FOUND); });
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
    public void inviteUser(Long groupId, String senderToken, Long receiverId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
        String senderHandle = jwtService.extractByUserHandle(senderToken); //get sender handle from token
        User sender = userRepository.findUserByUserHandle(senderHandle).orElseThrow(
                () -> new SubmitException("sender not found", HttpStatus.NOT_FOUND)
        );
        User receiver = userRepository.findById(receiverId).orElseThrow(
                () -> new SubmitException("Receiver not found", HttpStatus.NOT_FOUND)
        );
        invitationRepository.save(Invitation.builder()
                .receiver(receiver)
                .sender(sender)
                .group(group)
                .date(LocalDate.now())
                .status(InvitationStatus.PENDING)
                .build());
    }

    @Override
    @Transactional
    public void join(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new SubmitException("User not found", HttpStatus.NOT_FOUND)
        );
        // Check if the user is not already in the group
        if (userGroupRepository.existsByUserAndGroup(user, group)) {
            throw new SubmitException("User is already in the group", HttpStatus.ALREADY_REPORTED);
        }
        userGroupRepository.save(UserGroup.builder()
                .user(user)
                .group(group)
                .joinDate(LocalDate.now())
                .role(UserRole.MEMBER).build());
    }

    @Override
    @Transactional
    public void leave(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new SubmitException("Group not found", HttpStatus.NOT_FOUND)
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new SubmitException("User not found", HttpStatus.NOT_FOUND)
        );
        UserGroup userGroup = userGroupRepository.findByUserAndGroup(user, group).orElseThrow(
                () -> new SubmitException("User not found in the group", HttpStatus.NOT_FOUND)
        );
        userGroupRepository.deleteById(userGroup.getId());
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
}
