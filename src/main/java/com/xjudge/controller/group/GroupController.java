package com.xjudge.controller.group;

import com.xjudge.model.Pagination.PaginationResponse;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.model.invitation.InvitationRequest;
import com.xjudge.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/public")
    public ResponseEntity<?> getAllPublicGroups(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<GroupModel> pagedResult = groupService.getAllGroups(paging);
        PaginationResponse<GroupModel> response = new PaginationResponse<>(pagedResult.getTotalPages(), pagedResult.getContent());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.isPublic(#groupId) || @groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getSpecificGroup(@PathVariable  Long groupId) {
        return ResponseEntity.ok(groupService.getSpecificGroup(groupId));
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest, Principal connectedUser) {
        return ResponseEntity.ok(groupService.create(groupRequest, connectedUser));
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER', 'MANAGER'})")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest) {
        return ResponseEntity.ok(groupService.update(groupId, groupRequest));
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasRole(principal.username, #groupId, 'LEADER')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok("Group with ID " + groupId + " deleted successfully.");
    }

    @PostMapping("/{groupId}/contest/{contestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> addContest(@PathVariable Long groupId, @PathVariable Long contestId) {
        groupService.addContest(contestId, groupId);
        return ResponseEntity.ok("Contest added successfully.");
    }

    @PostMapping("/invite")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #invitationRequest.groupId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> inviteUserToGroup(@RequestBody InvitationRequest invitationRequest, Principal connectedUser) {
        groupService.inviteUser(invitationRequest.getGroupId(), invitationRequest.getReceiverId(), connectedUser);
        return ResponseEntity.ok("Invitation sent successfully.");
    }

    @PostMapping("/accept-invitation/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.acceptInvitation(invitationId, connectedUser);
        return ResponseEntity.ok("Invitation accepted successfully.");
    }

    @PostMapping("/decline-invitation/{invitationId}")
    public ResponseEntity<?> declineInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.declineInvitation(invitationId, connectedUser);
        return ResponseEntity.ok("Invitation declined successfully.");
    }

    @PostMapping("/{groupId}/join") // request private group join
    public ResponseEntity<?> joinUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.join(groupId, connectedUser);
        return ResponseEntity.ok("User joined successfully.");
    }

    @PostMapping("/{groupId}/leave") // security handled in user group service layer ✅
    public ResponseEntity<?> leaveUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.leave(groupId, connectedUser);
        return ResponseEntity.ok("User left successfully.");
    }

    @GetMapping("/{groupId}/contests")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupContests(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.Contests(groupId));
    }

    @GetMapping("/{groupId}/users")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupUsers(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.Users(groupId));
    }

}
