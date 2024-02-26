package com.xjudge.service.token;

import com.xjudge.entity.Token;

public interface TokenService {
    void save(Token token);
    Token findByToken(String token);
}
