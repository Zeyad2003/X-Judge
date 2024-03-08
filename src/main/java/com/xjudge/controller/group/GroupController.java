package com.xjudge.controller.group;

import com.xjudge.entity.Contest;
import com.xjudge.entity.User;
import com.xjudge.model.Pagination.PaginationResponse;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.model.invitation.InvitationRequest;
import com.xjudge.model.response.Response;
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
import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/public")
    public ResponseEntity<?> getAllGroups(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<GroupModel> pagedResult = groupService.getAllGroups(paging);
        PaginationResponse<GroupModel> paginatedData = new PaginationResponse<>(pagedResult.getTotalPages(), pagedResult.getContent());
        Response response = new Response(HttpStatus.OK.value(), true, paginatedData, "Groups fetched successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getSpecificGroup(@PathVariable  Long groupId) {
        GroupModel group = groupService.getSpecificGroup(groupId);
        Response response = new Response(HttpStatus.OK.value(), true, group, "Group fetched successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest, Principal connectedUser) {
        GroupModel group = groupService.create(groupRequest, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, group, "Group created successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER', 'MANAGER'})")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest) {
        GroupModel group = groupService.update(groupId, groupRequest);
        Response response = new Response(HttpStatus.OK.value(), true, group, "Group updated successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasRole(principal.username, #groupId, 'LEADER')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        Response response = new Response(HttpStatus.OK.value(), true, "Group with ID " + groupId + " deleted successfully.", "Group deleted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/contest/{contestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> addContest(@PathVariable Long groupId, @PathVariable Long contestId) {
        groupService.addContest(contestId, groupId);
        Response response = new Response(HttpStatus.OK.value(), true, "Contest added successfully.", "Contest added successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/invite")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #invitationRequest.groupId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> inviteUserToGroup(@RequestBody InvitationRequest invitationRequest, Principal connectedUser) {
        groupService.inviteUser(invitationRequest.getGroupId(), invitationRequest.getReceiverId(), connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "Invitation sent successfully.", "Invitation sent successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept-invitation/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.acceptInvitation(invitationId, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "Invitation accepted successfully.", "Invitation accepted successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/decline-invitation/{invitationId}")
    public ResponseEntity<?> declineInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.declineInvitation(invitationId, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "Invitation declined successfully.", "Invitation declined successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/request-join/{groupId}")
    public ResponseEntity<?> requestJoin(@PathVariable Long groupId, Principal connectedUser) {
        groupService.requestJoin(groupId, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "Request sent successfully.", "Request sent successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept-request/{requestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #requestId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {
        groupService.acceptRequest(requestId);
        return ResponseEntity.ok("Request accepted successfully.");
    }

    @PostMapping("/decline-request/{requestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #requestId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> declineRequest(@PathVariable Long requestId) {
        groupService.declineRequest(requestId);
        Response response = new Response(HttpStatus.OK.value(), true, "Request declined successfully.", "Request declined successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/join") // request private group join
    public ResponseEntity<?> joinUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.join(groupId, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "User joined successfully.", "User joined successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/leave") // security handled in user group service layer ✅
    public ResponseEntity<?> leaveUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.leave(groupId, connectedUser);
        Response response = new Response(HttpStatus.OK.value(), true, "User left successfully.", "User left successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/contests")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupContests(@PathVariable Long groupId) {
        List<Contest> contests = groupService.Contests(groupId);
        Response response = new Response(HttpStatus.OK.value(), true, contests, "Contests fetched successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/users")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupUsers(@PathVariable Long groupId) {
        List<User> users = groupService.Users(groupId);
        Response response = new Response(HttpStatus.OK.value(), true, users, "Users fetched successfully.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
