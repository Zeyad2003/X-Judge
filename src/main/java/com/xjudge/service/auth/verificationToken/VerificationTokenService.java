package com.xjudge.service.auth.verificationToken;

import com.xjudge.entity.VerificationToken;

public interface VerificationTokenService {
    void save(VerificationToken verificationToken);
    VerificationToken findByToken(String token);
}
