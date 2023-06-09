package com.example.carspacesdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.carspacesdemo.mapper")
public class CarSpacesDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarSpacesDemoApplication.class, args);
    }

}
