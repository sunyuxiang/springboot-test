package com.example.demo.job;

@Slf4j
@Component
public class MyJob {

    private Integer count1 = 0;

    private Integer count2 = 0;

    @Autowired
    private JobMetrics jobMetrics;

    @Async("main")
    @Scheduled(fixedDelay = 1000)
    public void doSomething() {
        count1++;
        jobMetrics.job1Counter.increment();
        jobMetrics.map.put("x", Double.valueOf(count1));
        System.out.println("task1 count:" + count1);
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public void doSomethingOther() {
        count2++;
        jobMetrics.job2Counter.increment();
        System.out.println("task2 count:" + count2);
    }
}
--------------------- 
作者：MyHerux 
来源：CSDN 
原文：https://blog.csdn.net/myherux/article/details/81781199 
版权声明：本文为博主原创文章，转载请附上博文链接！