package com.xjudge.controller.user;

import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.model.response.Response;
import com.xjudge.model.user.UserModel;
import com.xjudge.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "The end-points related to user operations.")
public class UserController {

    private final UserService userService;

    @GetMapping("/{handle}")
    public ResponseEntity<Response> getUserByHandle(@PathVariable String handle) {
        UserModel user = userService.findUserModelByHandle(handle);
        return ResponseEntity.ok(Response.builder()
                .success(true)
                .data(user)
                .message("Get user by handle successfully")
                .build());
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserModel user, Errors errors, Authentication authentication) {
        if (errors.hasErrors()) {
            throw new XJudgeValidationException(errors.getFieldErrors(), "Validation failed", UserController.class.getName(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(Response.builder()
                .success(true)
                .data(userService.updateUserByHandle(authentication.getName(), user))
                .message("User updated successfully")
                .build());
    }

    @GetMapping("/invitations")
    public ResponseEntity<?> GetUserInvitation(Principal connectedUser) {
        return ResponseEntity.ok(Response.builder()
                .success(true)
                .data(userService.getUserInvitations(connectedUser.getName()))
                .message("Get user invitations successfully")
                .build());
    }

}
