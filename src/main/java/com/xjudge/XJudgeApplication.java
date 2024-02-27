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
//
//    @Bean
//    public CommandLineRunner commandLineRunner(UserRepo userRepo, ContestRepo contestRepo , PasswordEncoder encoder) {
//        return args -> {
//            User user = User.builder()
//                    .id(1L)
//                    .role(UserRole.ADMIN)
//                    .handle("Zeyad_Nasef")
//                    .email("zeyad@gmail.com")
//                    .password(encoder.encode("123456"))
//                    .firstName("Zeyad")
//                    .lastName("Nasef")
//                    .school("MUFCI")
//                    .registrationDate(LocalDate.now())
//                    .solvedCount(0L)
//                    .attemptedCount(0L)
//                    .build();
//            if(userRepo.findByHandle(user.getHandle()).isEmpty()) userRepo.save(user);
//        };
//    }
}
