package com.xjudge.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstResource {
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
    @GetMapping("/")
    public String welcome() {
        return hello;
    }
}
