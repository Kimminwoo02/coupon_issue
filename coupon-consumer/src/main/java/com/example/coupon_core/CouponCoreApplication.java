package com.example.coupon_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
public class CouponCoreApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name","application-core,application-consumer");
		SpringApplication.run(CouponCoreApplication.class, args);
	}

}
