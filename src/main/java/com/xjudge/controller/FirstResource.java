package com.xjudge.controller;

import com.xjudge.repository.GroupRepository;
import com.xjudge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FirstResource {
    private final UserService userService;
    private final GroupRepository groupRepository;
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


    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findUserModelById(id) , HttpStatus.OK);
    }

    @GetMapping("/group/{id}/contests")
    public ResponseEntity<?> getGroupContest(@PathVariable("id") Long id) {
        return new ResponseEntity<>(groupRepository.findById(id).get().getGroupContests() , HttpStatus.OK);
    }

}
