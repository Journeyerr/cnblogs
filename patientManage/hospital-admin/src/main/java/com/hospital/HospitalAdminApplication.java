package com.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hospital.repository")
public class HospitalAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalAdminApplication.class, args);
    }
} 