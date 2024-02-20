package com.xjudge.service.auth.resetPasswordToken;

import com.xjudge.entity.PasswordResetToken;

public interface ResetPasswordService {
    void save(PasswordResetToken resetPasswordToken);
    PasswordResetToken findByToken(String token);

}
