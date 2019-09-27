package com.br.activiti;

import lombok.extern.slf4j.Slf4j;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@Configuration
@Slf4j
public class StartApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));//系统的classpaht路径
        System.out.println(System.getProperty("user.dir"));//用户的当前路径
        SpringApplication.run(StartApplication.class);
        //打印class path

    }
}
