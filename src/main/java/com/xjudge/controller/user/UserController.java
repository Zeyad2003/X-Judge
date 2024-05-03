package com.xjudge.controller.user;

import com.xjudge.model.response.Response;
import com.xjudge.model.user.UserModel;
import com.xjudge.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
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

}
