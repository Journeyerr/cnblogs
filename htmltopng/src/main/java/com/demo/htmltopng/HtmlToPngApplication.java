package com.demo.htmltopng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.demo.htmltopng.repository")
@SpringBootApplication
public class HtmlToPngApplication {

    public static void main(String[] args) {
        SpringApplication.run(HtmlToPngApplication.class, args);
    }

}
