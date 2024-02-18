package com.xjudge.controller.auth;

import com.xjudge.model.auth.*;
import com.xjudge.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest , BindingResult result){
        return new ResponseEntity<>(authService.register(registerRequest, result) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> loginAuth(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        return new ResponseEntity<>(authService.authenticate(loginRequest, result) , HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    ResponseEntity<String> verify(@RequestParam String token){
        return new ResponseEntity<>(authService.verifyRegistrationToken(token) , HttpStatus.OK);
    }

    @PostMapping("/change-password")
    ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal connectedUser){
        authService.changePassword(changePasswordRequest, connectedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    String get(){
        return "SERVER IS WORKING";
    }
}
