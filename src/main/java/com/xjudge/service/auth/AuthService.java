package com.xjudge.service.auth;

import com.xjudge.model.auth.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest, BindingResult bindingResult);
    LoginResponse authenticate(LoginRequest loginRequest, BindingResult bindingResult);
    String verifyRegistrationToken(String token, HttpServletResponse response, RedirectAttributes redirectAttributes);
    AuthResponse changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser);
    AuthResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    AuthResponse resetPassword(ResetPasswordRequest resetPasswordRequest);
}
