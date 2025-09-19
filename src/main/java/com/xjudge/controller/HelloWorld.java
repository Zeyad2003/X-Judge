package com.xjudge.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test the application is up and running.
 */

@RestController
@RequestMapping("/hello")
public class HelloWorld {
    @RequestMapping
    public String hello() {
        return "Hello World!";
    }
}
