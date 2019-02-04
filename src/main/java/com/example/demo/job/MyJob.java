package com.example.demo.job;

import com.example.demo.metrics.Meters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class MyJob {

    @Resource
    private Meters meters;

    @Async("main")
    @Scheduled(initialDelay = 1000, fixedRate = 2000)
    public void doSomething() {
        meters.getCounter("abc").increment();
    }

}
