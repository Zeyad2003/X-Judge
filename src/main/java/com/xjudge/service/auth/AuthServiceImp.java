package com.xjudge.service.auth;

import com.xjudge.entity.User;
import com.xjudge.enums.UserRole;
import com.xjudge.exception.SubmitException;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public AuthResponse register(UserRegisterRequest registerRequest) {

        // Check if user already exists
        Optional<User> existingUser = userRepo.findUserByUserHandle(registerRequest.getUserHandle());
        if (existingUser.isPresent()){
            throw new SubmitException("User already exists", HttpStatus.ALREADY_REPORTED);
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
                .token(token)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserHandle() , authRequest.getUserPassword())
            );
        } catch (AuthenticationException e) {
            throw new SubmitException("Username or password is incorrect" , HttpStatus.UNAUTHORIZED);
        }

        User user = userRepo
                .findUserByUserHandle(authRequest.getUserHandle())
                .orElseThrow(()-> new UsernameNotFoundException("USER NOT FOUND"));
        String token = jwtService.generateToken(user);
        return AuthResponse
                .builder()
                .token(token)
                .build();
    }
}
