package com.example.demo.controller;

import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.function.ToDoubleFunction;

@RestController
public class HelloWorldController {

    public static final Random RANDOM = new Random(System.currentTimeMillis());

    @RequestMapping("/hello")
    public String index() {
        Metrics.counter("hello").increment();
        return "Hello World";
    }

    @RequestMapping("/gauge")
    public String gauge() throws InterruptedException {
        Metrics.gauge("threadNumbers", RANDOM, value -> value.nextInt(100));
        return "ok";
    }
}