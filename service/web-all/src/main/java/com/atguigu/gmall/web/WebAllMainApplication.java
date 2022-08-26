package com.atguigu.gmall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableDiscoveryClient
//@EnableCircuitBreaker

@EnableFeignClients
@SpringCloudApplication
public class WebAllMainApplication {
    public static void main(String[] args) {

        SpringApplication.run(WebAllMainApplication.class,args);
    }
}
