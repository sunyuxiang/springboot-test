package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloWorldController {

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    @RequestMapping("/aaa")
    public Object index() {
//        return "Hello World";
        throw new RuntimeException("xxx");
    }

}