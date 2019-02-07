package com.example.demo.controller;

import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

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
        int i = 0;
        while (i < 100) {
            Thread.sleep(1000);
            Metrics.gauge("threadNumbers", RANDOM.nextInt(100));
        }
        return "ok";
    }
}