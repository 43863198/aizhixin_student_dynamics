package com.aizhixin.cloud.dataanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringCloudApplication
@EnableAsync
@EnableScheduling
@EnableEurekaClient
@EnableFeignClients
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
