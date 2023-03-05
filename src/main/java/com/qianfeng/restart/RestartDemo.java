package com.qianfeng.restart;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestartDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("restartReader")
	private ItemReader<? extends Customer> restartReader;
	@Autowired
	@Qualifier("restartWriter")
	private ItemWriter<? super Customer> restartWriter;
	

	@Bean
	public Job restartJob() {
		return jobBuilderFactory.get("restartJob")
				.start(restartStep())
				.build();
		
	}


	@Bean
	public Step restartStep() {
		return stepBuilderFactory.get("restartStep")
				.<Customer,Customer>chunk(10)
				.reader(restartReader)
				.writer(restartWriter)
				.build();
	}
	
}
