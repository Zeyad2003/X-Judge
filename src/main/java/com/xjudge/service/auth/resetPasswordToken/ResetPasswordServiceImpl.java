package com.xjudge.service.auth.resetPasswordToken;

import com.xjudge.entity.PasswordResetToken;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public void save(PasswordResetToken resetPasswordToken) {
        passwordResetTokenRepository.save(resetPasswordToken);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new AuthException("Invalid token", HttpStatus.BAD_REQUEST, new HashMap<>())
                );
    }
}
