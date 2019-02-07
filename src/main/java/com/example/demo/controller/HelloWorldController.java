package com.example.demo.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    @Timed(value = "xxxx", longTask = true, histogram = true)
    public String index() {
        return "Hello World";
    }
}