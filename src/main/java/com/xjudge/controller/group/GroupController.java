package com.xjudge.controller.group;

import com.xjudge.entity.Contest;
import com.xjudge.model.enums.UserGroupRole;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupMemberModel;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.model.invitation.InvitationRequest;
import com.xjudge.model.response.Response;
import com.xjudge.service.group.GroupService;
import com.xjudge.service.group.userGroupService.UserGroupService;
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
    private final UserGroupService userGroupService;
    @GetMapping("/public")
    public ResponseEntity<?> getAllGroups( Principal connectedUser,@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<GroupModel> paginatedData = groupService.getAllGroups(connectedUser,paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .message("Groups fetched successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/userHandle")
    public ResponseEntity<?> getGroupsByUserHandle(
            Principal connectedUser,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer size
    ) {
        Pageable paging = PageRequest.of(pageNo, size);
        String userHandle=connectedUser.getName();
        Page<GroupModel> paginatedData = groupService.getGroupsByUserHandle(userHandle, paging);
        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .message("Groups fetched successfully for user with handle: " + userHandle)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getSpecificGroup(@PathVariable  Long groupId) {
        GroupModel group = groupService.getSpecificGroup(groupId);
        Response response = Response.builder()
                .success(true)
                .data(group)
                .message("Group fetched successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/owned")
    public ResponseEntity<?> getGroupsOwnedByUser(Principal connectedUser) {
        List<GroupModel> groups = groupService.getGroupsOwnedByUser(connectedUser);
        Response response = Response.builder()
                .success(true)
                .data(groups)
                .message("Groups fetched successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody GroupRequest groupRequest, Principal connectedUser) {
        GroupModel group = groupService.create(groupRequest, connectedUser);
        Response response = Response.builder()
                .success(true)
                .data(group)
                .message("Group created successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER', 'MANAGER'})")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest) {
        GroupModel group = groupService.update(groupId, groupRequest);
        Response response = Response.builder()
                .success(true)
                .data(group)
                .message("Group updated successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasRole(principal.username, #groupId, 'LEADER')")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        Response response = Response.builder()
                .success(true)
                .message("Group with Id " + groupId + " deleted successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/invite")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #invitationRequest.groupId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> inviteUserToGroup(@RequestBody InvitationRequest invitationRequest, Principal connectedUser) {
        groupService.inviteUser(invitationRequest.getGroupId(), invitationRequest.getReceiverId(), connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("Invitation sent successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept-invitation/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.acceptInvitation(invitationId, connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("Invitation accepted successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/decline-invitation/{invitationId}")
    public ResponseEntity<?> declineInvitation(@PathVariable Long invitationId, Principal connectedUser) {
        groupService.declineInvitation(invitationId, connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("Invitation declined successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/request-join/{groupId}")
    public ResponseEntity<?> requestJoin(@PathVariable Long groupId, Principal connectedUser) {
        groupService.requestJoin(groupId, connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("Request sent successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/accept-request/{requestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #requestId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> acceptRequest(@PathVariable Long requestId) {
        groupService.acceptRequest(requestId);
        Response response = Response.builder()
                .success(true)
                .message("Request accepted successfully.")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decline-request/{requestId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #requestId, {'LEADER','MANAGER'})")
    public ResponseEntity<?> declineRequest(@PathVariable Long requestId) {
        groupService.declineRequest(requestId);
        Response response = Response.builder()
                .success(true)
                .message("Request declined successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/join") // request private group join
    public ResponseEntity<?> joinUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.join(groupId, connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("User joined successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{groupId}/leave") // security handled in user group service layer ✅
    public ResponseEntity<?> leaveUserGroup(@PathVariable Long groupId, Principal connectedUser) {
        groupService.leave(groupId, connectedUser);
        Response response = Response.builder()
                .success(true)
                .message("User left successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/contests")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupContests(@PathVariable Long groupId) {

        List<GroupContestModel> groupContests = groupService.getGroupContests(groupId);

        Response response = Response.builder()
                .success(true)
                .data(groupContests)
                .message("Group contests fetched successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{groupId}/members")
    @PreAuthorize("@groupSecurity.isMember(principal.username, #groupId)")
    public ResponseEntity<?> getGroupMembers(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer size) {

        Pageable paging = PageRequest.of(pageNo, size);
        Page<GroupMemberModel> groupMembers = groupService.getGroupMembers(groupId, paging);

        Response response = Response.builder()
                .success(true)
                .data(groupMembers)
                .message("Group members fetched successfully.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/userRole")
   public UserGroupRole getUserRole(Principal connectedUser, @PathVariable Long groupId){

        return userGroupService.findRoleByUserAndGroupId(connectedUser,groupId);
   }
}
