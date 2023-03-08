package com.qianfeng.skiplistener;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableBatchProcessing
public class SkipListenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SkipListenerApplication.class, args);
	}
}
