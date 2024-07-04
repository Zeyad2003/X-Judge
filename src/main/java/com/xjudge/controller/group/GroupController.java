package com.xjudge.controller.group;

import com.xjudge.entity.Group;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.group.GroupContestModel;
import com.xjudge.model.group.GroupMemberModel;
import com.xjudge.model.group.GroupModel;
import com.xjudge.model.group.GroupRequest;
import com.xjudge.model.invitation.InvitationRequest;
import com.xjudge.model.response.Response;
import com.xjudge.service.group.GroupService;
import com.xjudge.service.group.userGroupService.UserGroupService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@Tag(name = "Group", description = "The group end-points for handling group operations.")
public class GroupController {

    private final GroupService groupService;
    private final UserGroupService userGroupService;
    @GetMapping("/public")
    public ResponseEntity<?> getAllGroups( Principal connectedUser,@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "25") Integer size) {
        Pageable paging = PageRequest.of(pageNo, size);
        Page<GroupModel> paginatedData = groupService.getAllPublicGroups(connectedUser,paging);

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
    public ResponseEntity<?> getGroupById(Principal connectedUser, @PathVariable  Long groupId) {
        GroupModel group = groupService.getGroupById(groupId, connectedUser);
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
    public ResponseEntity<?> createGroup(@Valid @RequestBody GroupRequest groupRequest, Principal connectedUser, BindingResult bind) {
        if (bind.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bind.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
            throw new XJudgeValidationException(errorMap, "Validation failed", this.getClass().getName(), HttpStatus.BAD_REQUEST);
        }
        GroupModel group = groupService.create(groupRequest, connectedUser, bind);
        Response response = Response.builder()
                .success(true)
                .data(group)
                .message("Group created successfully.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{groupId}")
    @PreAuthorize("@groupSecurity.hasAnyRole(principal.username, #groupId, {'LEADER', 'MANAGER'})")
    public ResponseEntity<?> updateGroup(@PathVariable Long groupId, @Valid @RequestBody GroupRequest groupRequest) {
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
    public ResponseEntity<?> inviteUserToGroup(@Valid @RequestBody InvitationRequest invitationRequest, Principal connectedUser) {
        groupService.inviteUser(invitationRequest.getGroupId(), invitationRequest.getReceiverHandle(), connectedUser);
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
    @PreAuthorize("@groupSecurity.isPublicOrMember(principal.username, #groupId)")
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
    @PreAuthorize("@groupSecurity.isPublicOrMember(principal.username, #groupId)")
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

    @GetMapping("/userRole/{groupId}")
   public String getUserRole(Principal connectedUser, @PathVariable Long groupId){
        return userGroupService.findRoleByUserAndGroupId(connectedUser,groupId);
   }

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "25") Integer size) {

        Pageable paging = PageRequest.of(pageNo, size);
        Page<Group> paginatedData = groupService.searchGroupByName(name, paging);

        Response response = Response.builder()
                .success(true)
                .data(paginatedData)
                .message("Groups fetched successfully.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
