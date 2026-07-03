package com.studyhub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.studyhub.mapper")
public class StudyHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyHubApplication.class, args);
    }
}
