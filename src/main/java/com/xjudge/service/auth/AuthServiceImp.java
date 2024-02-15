package com.xjudge.service.auth;

import com.xjudge.entity.User;
import com.xjudge.enums.UserRole;
import com.xjudge.exception.auth.AuthException;
import com.xjudge.model.auth.AuthRequest;
import com.xjudge.model.auth.AuthResponse;
import com.xjudge.model.auth.UserRegisterRequest;
import com.xjudge.repository.UserRepo;
import com.xjudge.config.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImp implements AuthService{


    UserRepo userRepo;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImp(UserRepo repo , JwtService jwtService , PasswordEncoder passwordEncoder ,  AuthenticationManager authenticationManager){
        this.userRepo = repo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }
    @Override
    public AuthResponse register(UserRegisterRequest registerRequest, BindingResult bindingResult) {

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField() , error.getDefaultMessage());
            }
        }

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
                .build();


        userRepo.save(userDetails);

        Map<String , Object> claims = new HashMap<>();
        claims.put("email" , userDetails.getUserEmail());
        String token = jwtService.generateToken(claims , userDetails);

        return AuthResponse
                .builder()
                .statusCode(HttpStatus.CREATED.value())
                .token(token)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest, BindingResult bindingResult) {

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField() , error.getDefaultMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new AuthException("Authentication failed" , HttpStatus.BAD_REQUEST, errors);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserHandle() , authRequest.getUserPassword())
            );
        } catch (AuthenticationException e) {
            throw new AuthException("Username or password is incorrect" , HttpStatus.UNAUTHORIZED, errors);
        }

        User user = userRepo
                .findUserByUserHandle(authRequest.getUserHandle())
                .orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND"));
        String token = jwtService.generateToken(user);
        return AuthResponse
                .builder()
                .statusCode(HttpStatus.OK.value())
                .token(token)
                .build();
    }
}
