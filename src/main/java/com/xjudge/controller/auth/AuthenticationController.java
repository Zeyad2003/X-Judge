package com.xjudge.controller.auth;

import com.xjudge.model.auth.*;
import com.xjudge.model.response.Response;
import com.xjudge.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "The authorization end-points for handling user authentication operations.")
public class AuthenticationController {
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<Response> register(@Valid @RequestBody RegisterRequest registerRequest , BindingResult result){
        Response response = Response.builder()
                .success(true)
                .data(authService.register(registerRequest, result))
                .build();
        return new ResponseEntity<>(response , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    ResponseEntity<Response> loginAuth(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        Response response = Response.builder()
                .success(true)
                .data(authService.authenticate(loginRequest, result))
                .build();
        return new ResponseEntity<>(response , HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    ResponseEntity<Response> verify(@RequestParam String token, HttpServletResponse response, RedirectAttributes redirectAttributes){
        Response res = Response.builder()
                .success(true)
                .data(authService.verifyRegistrationToken(token, response, redirectAttributes))
                .build();
        return new ResponseEntity<>(res , HttpStatus.OK);
    }

    @PostMapping("/change-password")
    ResponseEntity<Response> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, Principal connectedUser){
        Response response = Response.builder()
                .success(true)
                .data(authService.changePassword(changePasswordRequest, connectedUser))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forget-password")
    ResponseEntity<Response> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        Response response = Response.builder()
                .success(true)
                .data(authService.forgotPassword(forgotPasswordRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    ResponseEntity<Response> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        Response response = Response.builder()
                .success(true)
                .data(authService.resetPassword(resetPasswordRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    String get(){
        return "SERVER IS WORKING";
    }
}
