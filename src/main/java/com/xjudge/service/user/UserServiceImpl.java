package com.xjudge.service.user;

import com.xjudge.entity.Token;
import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.exception.XJudgeValidationException;
import com.xjudge.mapper.UserMapper;
import com.xjudge.model.enums.TokenType;
import com.xjudge.model.invitation.InvitationModel;
import com.xjudge.model.user.UserModel;
import com.xjudge.repository.UserRepo;
import com.xjudge.service.email.EmailService;
import com.xjudge.service.invitiation.InvitationService;
import com.xjudge.service.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final InvitationService invitationService;

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findUserByHandle(String userHandle) {
        return userRepo.findByHandle(userHandle).orElseThrow(
                () -> new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelByHandle(String userHandle) {
        return userMapper.toModel(userRepo.findByHandle(userHandle).orElseThrow(
                () -> new XJudgeException("There's no handle {" + userHandle + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public User findUserByEmail(String userEmail) {
        return userRepo.findUserByEmail(userEmail).orElseThrow(
                () -> new XJudgeException("There's no email {" + userEmail + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelByEmail(String userEmail) {
        return userMapper.toModel(userRepo.findUserByEmail(userEmail).orElseThrow(
                () -> new XJudgeException("There's no email {" + userEmail + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public User findUserById(Long userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public UserModel findUserModelById(Long userId) {
        return userMapper.toModel(userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("There's no user with id {" + userId + "}", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        ));
    }

    @Override
    public UserModel updateUser(Long id, UserModel user) {
        User oldUser = userRepo.findById(id).orElseThrow(
                () -> new XJudgeException("User not found", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        oldUser.setHandle(user.getHandle());
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setEmail(user.getEmail());
        oldUser.setSchool(user.getSchool());
        oldUser.setPhotoUrl(user.getPhotoUrl());
        // api must store photo and return url
        return userMapper.toModel(oldUser);
    }

    @Override
    @Transactional
    public UserModel updateUserByHandle(String handle, UserModel user) {
        User oldUser = userRepo.findByHandle(handle).orElseThrow(
                () -> new XJudgeException("User not found", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        oldUser.setFirstName(user.getFirstName());
        oldUser.setLastName(user.getLastName());
        oldUser.setSchool(user.getSchool());

        if (!oldUser.getEmail().equals(user.getEmail())) {
            if (userRepo.existsByEmail(user.getEmail())) {
                List<FieldError> errors = new ArrayList<>();
                String defaultMessage = "Email already exists";
                FieldError error = new FieldError("email", "email", defaultMessage);
                errors.add(error);
                throw new XJudgeValidationException(errors, "Validation failed", UserServiceImpl.class.getName(), HttpStatus.BAD_REQUEST);
            }
            oldUser.setIsVerified(false);
            oldUser.setEmail(user.getEmail());
            generateTokenAndSendEmail(oldUser);
        }

        return userMapper.toModel(
                userRepo.save(oldUser)
        );
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new XJudgeException("User not found", UserServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        userRepo.delete(user);
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toModel)
                .toList();
    }

    @Override
    public boolean existsByHandle(String userHandle) {
        return userRepo.existsByHandle(userHandle);
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return userRepo.existsByEmail(userEmail);
    }

    @Override
    public List<InvitationModel> getUserInvitations(String handle) {
        return invitationService.getInvitationByReceiverHandle(handle);
    }

    @Transactional
    public void generateTokenAndSendEmail(User user) {
        String verificationToken = UUID.randomUUID().toString() + '-' + UUID.randomUUID();
        tokenService.save(Token.builder()
                .token(verificationToken)
                .user(user)
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .verifiedAt(null)
                .build());
        String emailContent = "<div style='font-family: Arial, sans-serif; width: 80%; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                + "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #ddd;'>"
                + "<h1>Email Change Request</h1>"
                + "</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Dear " + user.getUsername() + ",</p>"
                + "<p>e received a request to reset your password. Please click the link below to verify your email:<p>"
                + "<p><a href='http://localhost:7070/auth/verify-email?token=" + verificationToken + "'>Verify Email</a></p>"
                + "<p>If you did not register at XJudge, please ignore this email.</p>"
                + "<p>Best Regards,</p>"
                + "<p>The XJudge Team</p>"
                + "</div>"
                + "</div>";
        emailService.send(user.getEmail(), "Email Change Request", emailContent);
    }
}
