package com.xjudge.service.auth;

import com.xjudge.entity.Token;
import com.xjudge.entity.User;

import com.xjudge.mapper.UserMapper;
import com.xjudge.model.enums.TokenType;
import com.xjudge.model.enums.UserRole;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.model.auth.*;

import com.xjudge.config.security.JwtService;
import com.xjudge.model.user.UserModel;
import com.xjudge.service.email.EmailService;
import com.xjudge.service.token.TokenService;
import com.xjudge.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest, BindingResult bindingResult) {

        Map<String, String> errors = checkErrors(bindingResult);

        // Check if user with the same handle exists
        if (userService.existsByHandle(registerRequest.getUserHandle())) {
            errors.put("userHandle" , "User with this handle already exists");
        }

        // Check if user with the same email exists
        if (userService.existsByEmail(registerRequest.getUserEmail())) {
            errors.put("userEmail" , "User with this email already exists");
        }

        if (!errors.isEmpty()) {
            throw new AuthException("Registration failed" , HttpStatus.BAD_REQUEST, errors);
        }

        User userDetails = User.builder()
                .handle(registerRequest.getUserHandle())
                .password(passwordEncoder.encode(registerRequest.getUserPassword()))
                .email(registerRequest.getUserEmail())
                .firstName(registerRequest.getUserFirstName())
                .lastName(registerRequest.getUserLastName())
                .photoUrl(registerRequest.getUserPhotoUrl())
                .registrationDate(LocalDate.now())
                .school(registerRequest.getUserSchool())
                .photoUrl("http://localhost:7070/profile/Default_Image.jpg")
                .attemptedCount(0L)
                .solvedCount(0L)
                .role(UserRole.USER)
                .isVerified(false)
                .build();

        userService.save(userDetails);

        String verificationToken = UUID.randomUUID().toString() + '-' + UUID.randomUUID();
        String emailContent = "<div style='font-family: Arial, sans-serif; width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #ddd;'>"
                + "<h1>Welcome to XJudge</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Dear " + userDetails.getUsername() + ",</p>"
                + "<p>Thank you for registering at XJudge. Please click the link below to verify your email:</p>"
                + "<p><a href='http://localhost:7070/auth/verify-email?token=" + verificationToken + "'>Verify Email</a></p>"
                + "<p>If you did not register at XJudge, please ignore this email.</p>"
                + "<p>Best Regards,</p>"
                + "<p>The XJudge Team</p>"
                + "</div>"
                + "</div>";

        emailService.send(userDetails.getEmail() , "Email Verification" , emailContent);

        tokenService.save(Token.builder()
                .token(verificationToken)
                .user(userDetails)
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .verifiedAt(null)
                .build());

        return AuthResponse
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

        UserModel model = userService.findUserModelByHandle(loginRequest.getUserHandle());
        System.out.println(model);
        User user = userMapper.toEntity(model);
        System.out.println(user);

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
        //VerificationToken verificationToken = verificationTokenService.findByToken(token);
        Token verificationToken = tokenService.findByToken(token);

        if (verificationToken.getTokenType() != TokenType.EMAIL_VERIFICATION) {
            redirectAttributes.addAttribute("emailVerificationError", "Invalid token");
            redirectToLoginPage(response);
            return "Redirected to login page with error...";
        }

        if (verificationToken.getVerifiedAt() != null) {
            redirectAttributes.addAttribute("emailVerificationError", "Token already verified");
            redirectToLoginPage(response);
            return "Redirected to login page with error...";
        }

        if (verificationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            redirectAttributes.addAttribute("emailVerificationError", "Token expired");
            redirectToLoginPage(response);
            return "Redirected to login page with error...";
        }

        User user = verificationToken.getUser();
        user.setIsVerified(true);
        userService.save(user);

        verificationToken.setVerifiedAt(LocalDateTime.now());
        tokenService.save(verificationToken);

        redirectAttributes.addAttribute("emailVerificationSuccess", "Email verification successful. You can now login.");
        redirectToLoginPage(response);
        return "Redirected to login page...";
    }

    @Override
    public AuthResponse changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        User user = userMapper.toEntity(userService.findUserModelByHandle(connectedUser.getName()));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new AuthException("Old password is incorrect", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new AuthException("Passwords do not match", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new AuthException("New password cannot be the same as old password", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userService.save(user);

        return AuthResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password changed successfully")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userMapper.toEntity(userService.findUserModelByEmail(forgotPasswordRequest.getEmail()));
        String token = UUID.randomUUID().toString() + '-' + UUID.randomUUID();
        tokenService.save(Token.builder()
                .token(token)
                .user(user)
                .tokenType(TokenType.PASSWORD_RESET)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .verifiedAt(null)
                .build());

        String emailContent = "<div style='font-family: Arial, sans-serif; width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #ddd;'>"
                + "<h1>Password Reset Request</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Dear " + user.getUsername() + ",</p>"
                + "<p>We received a request to reset your password. Please click the link below to set a new password:</p>"
                + "<p><a href='http://localhost:4200/resetPassword?token=" + token + "'>Reset Password</a></p>"
                + "<p>If you did not request a password reset, please ignore this email.</p>"
                + "<p>Best Regards,</p>"
                + "<p>The xJudge Team</p>"
                + "</div>"
                + "</div>";

        emailService.send(user.getEmail(), "Reset Password", emailContent);

        return AuthResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reset password link has been sent to your email")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Token passwordResetToken = tokenService.findByToken(resetPasswordRequest.getToken());

        if (passwordResetToken.getTokenType() != TokenType.PASSWORD_RESET) {
            throw new AuthException("Invalid token", HttpStatus.BAD_REQUEST, new HashMap<>());
        }

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

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userService.save(user);

        passwordResetToken.setVerifiedAt(LocalDateTime.now());
        tokenService.save(passwordResetToken);

        return AuthResponse
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

    private void redirectToLoginPage(HttpServletResponse response) {
        try {
            response.sendRedirect("http://localhost:4200/login");
        } catch (IOException e) {
            throw new AuthException("Redirect failed", HttpStatus.INTERNAL_SERVER_ERROR, new HashMap<>());
        }
    }
}