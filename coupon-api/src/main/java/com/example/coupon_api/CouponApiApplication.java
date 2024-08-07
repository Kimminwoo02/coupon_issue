package com.example.coupon_api;

import com.example.coupon_core.CouponCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
public class CouponApiApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name","application-core,application-api");
		SpringApplication.run(CouponApiApplication.class, args);
	}

}
