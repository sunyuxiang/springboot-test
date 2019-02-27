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

    @RequestMapping("/")
    public String entrance() {
        return "welcome";
    }

    @RequestMapping("/gauge")
    public String gauge() throws InterruptedException {
        Metrics.gauge("threadNumbers", RANDOM, value -> value.nextInt(100));
        return "ok";
    }

    @RequestMapping("/timer")
    public String timer() throws InterruptedException {
        Metrics.timer("sleepTime").record(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return "ok";
    }

    @RequestMapping("/summary")
    public String summary() throws InterruptedException {
        int i = 0;
        while (i < 100) {
            Metrics.summary("ageSummary").record(RANDOM.nextInt(90));
        }
        return "ok";
    }
}