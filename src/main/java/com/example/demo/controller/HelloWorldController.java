package com.example.demo.controller;

import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String index() {
        Metrics.counter("hello").increment();
        return "Hello World";
    }

    @RequestMapping("/gauge")
    public String gauge() {
        Metrics.gauge("threadNumbers", 5);
        return "ok";
    }
}