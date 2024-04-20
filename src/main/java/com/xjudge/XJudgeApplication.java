package com.xjudge;

import com.xjudge.entity.User;
import com.xjudge.model.enums.UserRole;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class XJudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(XJudgeApplication.class, args);
    }

}
