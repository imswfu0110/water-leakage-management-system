package com.donghai.leakage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.donghai.leakage.mapper")
@SpringBootApplication
public class LeakageApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeakageApplication.class, args);
    }
}