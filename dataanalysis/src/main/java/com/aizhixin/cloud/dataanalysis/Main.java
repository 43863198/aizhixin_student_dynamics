package com.aizhixin.cloud.dataanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringCloudApplication
@EnableAsync
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
