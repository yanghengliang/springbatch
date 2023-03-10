package com.qianfeng.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @ClassName: ChildJob1
 * @Description: 子任务
 *　@author: yang
 * @date: 2023/02/25
 *
 */
@Configuration
@EnableBatchProcessing
public class ChildJob2 {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	
	@Bean
	public Step childJob2Step1() {
		return stepBuilderFactory.get("childJob2Step1").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("■childJob2Step1");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step childJob2Step2() {
		return stepBuilderFactory.get("childJob2Step2").tasklet(new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("■childJob2Step2");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Job childJobTwo() {
		return jobBuilderFactory.get("childJobTwo")
				.start(childJob2Step1())
				.next(childJob2Step2())
				.build();
	}
	
	
	
}
