package com.zzl.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Zzl
 * @Date: 21:22 2020/3/25
 * @Version 1.0
 **/
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(MainApplication.class, args);
    }
}
