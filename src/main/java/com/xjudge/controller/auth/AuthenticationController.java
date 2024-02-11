package com.xjudge.controller.auth;

import com.github.dockerjava.api.exception.BadRequestException;
import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.model.auth.UserRegisterRequest;
import com.xjudge.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest , BindingResult result){
        if(result.hasErrors()){
            throw new BadRequestException(result.getFieldErrors().toString());
        }
        return new ResponseEntity<>(authService.register(registerRequest) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponse> loginAuth(@Valid @RequestBody AuthRequest authRequest , BindingResult result){
        if(result.hasErrors()){
            throw new BadRequestException(result.getFieldErrors().toString());
        }
        return new ResponseEntity<>(authService.authenticate(authRequest) , HttpStatus.OK);
    }

    @GetMapping
    String get(){
        return "SERVER IS WORKING";
    }
}
