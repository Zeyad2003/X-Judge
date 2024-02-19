package com.xjudge;

import com.xjudge.entity.User;
import com.xjudge.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class XJudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(XJudgeApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepo userRepo) {
        return args -> {
            User user = User.builder()
                    .id(1L)
                    .handle("Zeyad_Nasef")
                    .email("zeyad@gmail.com")
                    .password("123456")
                    .firstName("Zeyad")
                    .lastName("Nasef")
                    .school("MUFCI")
                    .registrationDate(LocalDate.now())
                    .solvedCount(0L)
                    .attemptedCount(0L)
                    .build();
            userRepo.save(user);
        };
    }
}
