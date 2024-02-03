package com.xjudge.controller.auth;

import lombok.RequiredArgsConstructor;

import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.service.auth.AuthService;
import com.xjudge.model.auth.UserRegisterRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@RequestBody UserRegisterRequest registerRequest){
        return new ResponseEntity<>(authService.register(registerRequest) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> loginAuth(@RequestBody AuthRequest authRequest){
        return new ResponseEntity<>(authService.authenticate(authRequest) , HttpStatus.OK);
    }

    @GetMapping
    String get(){
        return "SERVER IS WORKING";
    }
}
