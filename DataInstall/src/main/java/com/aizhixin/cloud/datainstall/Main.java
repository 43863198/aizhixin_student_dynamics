package com.aizhixin.cloud.datainstall;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringCloudApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
