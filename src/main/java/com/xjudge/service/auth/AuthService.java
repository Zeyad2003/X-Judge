package com.xjudge.service.auth;

import com.xjudge.models.AuthRequest;
import com.xjudge.models.AuthResponse;
import com.xjudge.models.UserRegisterRequest;

public interface AuthService {
    AuthResponse register(UserRegisterRequest registerRequest);
    AuthResponse authenticate(AuthRequest authRequest);
}
