package com.xjudge.controller;

import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.repository.InvitationRepository;
import com.xjudge.repository.UserRepo;
import com.xjudge.service.group.GroupServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FirstResource {
    private final InvitationRepository invitationRepository;
    private final UserRepo userRepo;
    String hello = """
            <!DOCTYPE html>
            <html>
              <head>
                <style>
                  body {
                    background-color: #f2f2f2;
                    text-align: center;
                    font-family: Arial, sans-serif;
                  }
                  h1 {
                    color: #333;
                    font-size: 36px;
                  }
                  p {
                    color: #666;
                    font-size: 18px;
                  }
                </style>
              </head>
              <body>
                <h1>Welcome to X-Judge</h1>
                <p>The application is under development.</p>
                <p>Pray for us that we can finish it soon. \uD83D\uDE04</p>
              </body>
            </html>
            """;
    @GetMapping
    public String welcome() {
        return hello;
    }

    @GetMapping("/invitations/{id}")
    public ResponseEntity<?> testToGetUserInvitation(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow(
                () -> new XJudgeException("Lol", GroupServiceImpl.class.getName(), HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.ok(invitationRepository.getInvitationsByReceiver(user));
    }
}
