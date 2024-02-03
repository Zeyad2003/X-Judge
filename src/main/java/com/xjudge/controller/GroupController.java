package com.xjudge.controller;

import com.xjudge.config.security.JwtService;
import com.xjudge.entity.User;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.model.invitation.InvitationRequest;
import com.xjudge.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/public")
    public ResponseEntity<?> getAllPublicGroups() {
        return ResponseEntity.ok(groupService.publicGroups());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getSpecificGroup(@PathVariable  Long groupId) {
        return ResponseEntity.ok(groupService.getSpecificGroup(groupId));
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest) {
        return ResponseEntity.ok(groupService.create(groupRequest));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest) {
        return ResponseEntity.ok(groupService.update(groupId, groupRequest));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok("Group with ID " + groupId + " deleted successfully.");
    }

    @PostMapping("/{groupId}/contest/{contestId}")
    public ResponseEntity<?> addContest(@PathVariable Long groupId, @PathVariable Long contestId) {
        groupService.addContest(contestId, groupId);
        return ResponseEntity.ok("Contest added successfully.");
    }
////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUserToGroup(@RequestBody InvitationRequest invitationRequest) {
        groupService.inviteUser(invitationRequest.getGroupId(), invitationRequest.getToken(), invitationRequest.getReceiverId());
        return ResponseEntity.ok("Invitation sent successfully.");
    }

    @PostMapping("/{groupId}/join/{userId}")
    public ResponseEntity<?> joinUserGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.join(groupId, userId);
        return ResponseEntity.ok("User joined successfully.");
    }

    @PostMapping("/{groupId}/leave/{userId}")
    public ResponseEntity<?> leaveUserGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.leave(groupId, userId);
        return ResponseEntity.ok("User left successfully.");
    }

    @GetMapping("/{groupId}/contests")
    public ResponseEntity<?> getGroupContests(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.Contests(groupId));
    }

    @GetMapping("/{groupId}/users")
    public ResponseEntity<?> getGroupUsers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.Users(groupId));
    }

}
