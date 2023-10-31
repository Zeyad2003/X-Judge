package com.xjudge.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstResource {
  String hello =
      "<!DOCTYPE html>\n"
      + "<html>\n"
      + "  <head>\n"
      + "    <style>\n"
      + "      body {\n"
      + "        background-color: #f2f2f2;\n"
      + "        text-align: center;\n"
      + "        font-family: Arial, sans-serif;\n"
      + "      }\n"
      + "      h1 {\n"
      + "        color: #333;\n"
      + "        font-size: 36px;\n"
      + "      }\n"
      + "      p {\n"
      + "        color: #666;\n"
      + "        font-size: 18px;\n"
      + "      }\n"
      + "    </style>\n"
      + "  </head>\n"
      + "  <body>\n"
      + "    <h1>Welcome to X-Judge</h1>\n"
      + "    <p>The application is under development.</p>\n"
      + "    <p>Pray for us that we can finish it soon. \uD83D\uDE04</p>\n"
      + "  </body>\n"
      + "</html>\n";
  @GetMapping("/")
  public String welcome() {
    return hello;
  }
}
