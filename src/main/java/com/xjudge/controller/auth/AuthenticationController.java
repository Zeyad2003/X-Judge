package com.xjudge.controller.auth;

import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.model.auth.UserRegisterRequest;
import com.xjudge.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService){
        this.authService = authService;
    }

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
