package com.qianfeng.springbatch.config;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
 * @ClassName: ParametersDemo
 * @Description: 利用监听器，传递参数
 *　@author: yang
 * @date: 2023/02/27
 *
 */
@Configuration
@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener{
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	private Map<String, JobParameter> parameters;
	
	@Bean
	public Job parameterJob() {
		return jobBuilderFactory.get("parameterJob")
				.start(parameterStep())
				.build();
	}

	
	@Bean
	public Step parameterStep() {
		return stepBuilderFactory.get("parameterStep")
				.listener(this)
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						//输出接收到的参数的值
						System.out.println(parameters.get("info"));
						return RepeatStatus.FINISHED;
					}
				}).build();
	}


	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("■beforeStep");
		parameters = stepExecution.getJobParameters().getParameters();
	}


	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("■afterStep");
		return null;
	}
}
