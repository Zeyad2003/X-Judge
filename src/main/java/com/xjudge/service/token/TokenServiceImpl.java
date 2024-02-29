package com.xjudge.service.token;

import com.xjudge.entity.Token;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.TokenRepository;
import com.xjudge.service.group.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token).orElseThrow(
                () -> new XJudgeException("Invalid token", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }
}
