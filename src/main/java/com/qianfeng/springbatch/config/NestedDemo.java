package com.qianfeng.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 
 * @ClassName: NestedDemo
 * @Description: Job嵌套
 *               
 * 
 *　@author: yang
 * @date: 2023/02/25
 *
 */
@Configuration
@EnableBatchProcessing
public class NestedDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private Job childJobOne;
	@Autowired
	private Job childJobTwo;
	
	@Autowired
	private JobLauncher JobLauncher;
	
	
	@Bean
	public Job parentJob(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return jobBuilderFactory.get("parentJob")
				.start(childJob1(jobRepository,platformTransactionManager))
				.next(childJob2(jobRepository,platformTransactionManager))
				.build();
	}

	// Job类型转Step类型
	private Step childJob1(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob1"))
				.job(childJobOne)
				.launcher(JobLauncher)
				.repository(jobRepository)
				.transactionManager(platformTransactionManager)
				.build();
	}
	


	private Step childJob2(JobRepository jobRepository,PlatformTransactionManager platformTransactionManager) {
		return new JobStepBuilder(new StepBuilder("childJob2"))
				.job(childJobTwo)
				.launcher(JobLauncher)
				.repository(jobRepository)
				.transactionManager(platformTransactionManager)
				.build();
	}

	
}
