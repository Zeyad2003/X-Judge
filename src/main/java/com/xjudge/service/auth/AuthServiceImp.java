package com.xjudge.service.auth;

import com.xjudge.entity.PasswordResetToken;
import com.xjudge.entity.User;
import com.xjudge.entity.VerificationToken;
import com.xjudge.enums.UserRole;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.model.auth.*;
import com.xjudge.repository.UserRepo;
import com.xjudge.config.security.JwtService;
import com.xjudge.service.auth.resetPasswordToken.ResetPasswordService;
import com.xjudge.service.auth.verificationToken.VerificationTokenService;
import com.xjudge.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImp implements AuthService{


    UserRepo userRepo; // Use service layer instead of repository layer directly
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    EmailService emailService;
    VerificationTokenService verificationTokenService;
    ResetPasswordService resetPasswordService;

    @Autowired
    public AuthServiceImp(UserRepo userRepo,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          EmailService emailService,
                          VerificationTokenService verificationTokenService,
                          ResetPasswordService resetPasswordService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.verificationTokenService = verificationTokenService;
        this.resetPasswordService = resetPasswordService;
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest, BindingResult bindingResult) {

        Map<String, String> errors = checkErrors(bindingResult);

        // Check if user with the same handle exists
        if (userRepo.existsByUserHandle(registerRequest.getUserHandle())) {
            errors.put("userHandle" , "User with this handle already exists");
        }

        // Check if user with the same email exists
        if (userRepo.existsByUserEmail(registerRequest.getUserEmail())) {
            errors.put("userEmail" , "User with this email already exists");
        }

        if (!errors.isEmpty()) {
            throw new AuthException("Registration failed" , HttpStatus.BAD_REQUEST, errors);
        }

        User userDetails = User.builder()
                .userHandle(registerRequest.getUserHandle())
                .userPassword(passwordEncoder.encode(registerRequest.getUserPassword()))
                .userEmail(registerRequest.getUserEmail())
                .userFirstName(registerRequest.getUserFirstName())
                .userLastName(registerRequest.getUserLastName())
                .userPhotoUrl(registerRequest.getUserPhotoUrl())
                .userRegistrationDate(LocalDate.now())
                .userSchool(registerRequest.getUserSchool())
                .role(UserRole.USER)
                .isVerified(false)
                .build();

        userRepo.save(userDetails);

        String verificationToken = UUID.randomUUID().toString() + UUID.randomUUID();
        String emailContent = "<div style='font-family: Arial, sans-serif; width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #ddd;'>"
                + "<h1>Welcome to xJudge</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Dear " + userDetails.getUserFirstName() + ",</p>"
                + "<p>Thank you for registering at xJudge. Please click the link below to verify your email:</p>"
                + "<p><a href='http://localhost:7070/auth/verify-email?token=" + verificationToken + "'>Verify Email</a></p>"
                + "<p>If you did not register at xJudge, please ignore this email.</p>"
                + "<p>Best Regards,</p>"
                + "<p>The xJudge Team</p>"
                + "</div>"
                + "</div>";

        emailService.send(userDetails.getUserEmail() , "Email Verification" , emailContent);

        verificationTokenService.save(VerificationToken.builder()
                .token(verificationToken)
                .user(userDetails)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .verifiedAt(null)
                .build());

        return RegisterResponse
                .builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("User registered successfully, please verify your email to login")
                .build();
    }

    @Override
    public LoginResponse authenticate(LoginRequest loginRequest, BindingResult bindingResult) {

        Map<String, String> errors = checkErrors(bindingResult);

        if (!errors.isEmpty()) {
            throw new AuthException("Authentication failed" , HttpStatus.BAD_REQUEST, errors);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserHandle() , loginRequest.getUserPassword())
            );
        } catch (AuthenticationException e) {
            throw new AuthException("Username or password is incorrect" , HttpStatus.UNAUTHORIZED, errors);
        }

        User user = userRepo
                .findUserByUserHandle(loginRequest.getUserHandle())
                .orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND"));

        if (!user.isVerified()) {
            throw new AuthException("Email not verified" , HttpStatus.UNAUTHORIZED, errors);
        }
        String token = jwtService.generateToken(user);
        return LoginResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public String verifyRegistrationToken(String token, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);

        if (verificationToken.getVerifiedAt() != null) {
            throw new AuthException("Token already verified", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (verificationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AuthException("Token expired", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        User user = verificationToken.getUser();
        user.setVerified(true);
        userRepo.save(user);

        verificationToken.setVerifiedAt(LocalDateTime.now());
        verificationTokenService.save(verificationToken);

        redirectAttributes.addFlashAttribute("emailVerificationMessage", "Email verification successful. You can now login.");
        try {
            response.sendRedirect("http://localhost:4200/login");
        } catch (IOException e) {
            throw new AuthException("Redirect failed", HttpStatus.INTERNAL_SERVER_ERROR, new HashMap<>());
        }
        return "Redirected to login page...";
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getUserPassword())) {
            throw new AuthException("Old password is incorrect", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new AuthException("Passwords do not match", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new AuthException("New password cannot be the same as old password", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        user.setUserPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepo.save(user);
        return ChangePasswordResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .build();
    }

    @Override
    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepo.findUserByUserEmail(forgotPasswordRequest.getEmail()).orElseThrow(
                () -> new AuthException("User with this email does not exist", HttpStatus.NOT_FOUND, new HashMap<>())
        );

        String token = UUID.randomUUID().toString() + UUID.randomUUID();
        resetPasswordService.save(PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .verifiedAt(null)
                .build());

        String emailContent = "<div style='font-family: Arial, sans-serif; width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #ddd;'>"
                + "<h1>Password Reset Request</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Dear " + user.getUserFirstName() + ",</p>"
                + "<p>We received a request to reset your password. Please click the link below to set a new password:</p>"
                + "<p><a href='http://localhost:4200/resetPasswordtoken=" + token + "'>Reset Password</a></p>"
                + "<p>If you did not request a password reset, please ignore this email.</p>"
                + "<p>Best Regards,</p>"
                + "<p>The xJudge Team</p>"
                + "</div>"
                + "</div>";

        emailService.send(user.getUserEmail(), "Reset Password", emailContent);

        return ForgotPasswordResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reset password link has been sent to your email")
                .build();
    }

    @Override
    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = resetPasswordService.findByToken(resetPasswordRequest.getToken());

        if (passwordResetToken.getVerifiedAt() != null) {
            throw new AuthException("Token already used", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (passwordResetToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new AuthException("Token expired", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        User user = passwordResetToken.getUser();

        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new AuthException("Passwords do not match", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        user.setUserPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepo.save(user);

        passwordResetToken.setVerifiedAt(LocalDateTime.now());
        resetPasswordService.save(passwordResetToken);

        return ResetPasswordResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password reset successfully")
                .build();
    }

    private Map<String, String> checkErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
        }
        return errors;
    }
}
