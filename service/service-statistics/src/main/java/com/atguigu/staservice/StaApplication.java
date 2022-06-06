package com.atguigu.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient   //nacos注册注解
@EnableFeignClients  //远程调用端注解
@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"}) //进行组件扫描，将其他配置类扫描放入
@MapperScan("com.atguigu.staservice.mapper")
@EnableScheduling //支持定时任务的注解
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class, args);
    }
}
