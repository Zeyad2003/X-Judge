package com.xjudge.service.auth;

import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.model.auth.UserRegisterRequest;

public interface AuthService {
    AuthResponse register(UserRegisterRequest registerRequest);
    AuthResponse authenticate(AuthRequest authRequest);
}
