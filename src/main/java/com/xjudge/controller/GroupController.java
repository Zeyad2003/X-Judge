package com.xjudge.controller;

import com.xjudge.model.group.GroupRequest;
import com.xjudge.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<?> getAllPublicGroups() {
        return ResponseEntity.ok(groupService.publicGroups());
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<?> getSpecificGroup(@PathVariable  Long groupId) {
        return ResponseEntity.ok(groupService.details(groupId));
    }

    @PostMapping
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

    @PostMapping("/{groupId}/user/{userId}")
    public ResponseEntity<?> inviteUserToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.inviteUser(groupId, userId);
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
