package com.xjudge.service.auth;

import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.model.auth.UserRegisterRequest;
import org.springframework.validation.BindingResult;

public interface AuthService {
    AuthResponse register(UserRegisterRequest registerRequest, BindingResult bindingResult);
    AuthResponse authenticate(AuthRequest authRequest, BindingResult bindingResult);
}
