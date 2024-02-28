package com.xjudge.controller.auth;

import com.xjudge.model.auth.*;
import com.xjudge.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest , BindingResult result){
        return new ResponseEntity<>(authService.register(registerRequest, result) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> loginAuth(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        return new ResponseEntity<>(authService.authenticate(loginRequest, result) , HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    ResponseEntity<String> verify(@RequestParam String token, HttpServletResponse response, RedirectAttributes redirectAttributes){
        return new ResponseEntity<>(authService.verifyRegistrationToken(token, response, redirectAttributes) , HttpStatus.OK);
    }

    @PostMapping("/change-password")
    ResponseEntity<AuthResponse> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal connectedUser){
        return new ResponseEntity<>(authService.changePassword(changePasswordRequest, connectedUser), HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    ResponseEntity<AuthResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return new ResponseEntity<>(authService.forgotPassword(forgotPasswordRequest), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    ResponseEntity<AuthResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        return new ResponseEntity<>(authService.resetPassword(resetPasswordRequest), HttpStatus.OK);
    }

    @GetMapping
    String get(){
        return "SERVER IS WORKING";
    }
}
