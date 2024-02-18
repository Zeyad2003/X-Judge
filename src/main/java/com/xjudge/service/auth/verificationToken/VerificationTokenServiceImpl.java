package com.xjudge.service.auth.verificationToken;

import com.xjudge.entity.VerificationToken;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public void save(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new AuthException("Invalid verification token", HttpStatus.BAD_REQUEST, new HashMap<>())
        );
    }
}
