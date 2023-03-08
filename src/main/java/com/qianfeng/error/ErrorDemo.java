package com.qianfeng.error;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job errorsDemoJob() {
		System.out.println("■errorsDemoJob start");
		return jobBuilderFactory.get("errorsDemoJob")
				.start(errorStep1())
				.next(errorStep2())
				.build();
		
	}

	@Bean
	public Step errorStep1() {
		System.out.println("■errorStep1");
		return stepBuilderFactory.get("errorStep1")
				.tasklet(errorHandling())
				.build();
	}

	@Bean
	public Step errorStep2() {
		System.out.println("■errorStep2");
		return stepBuilderFactory.get("errorStep2")
				.tasklet(errorHandling())
				.build();
	}
	
	@Bean
	@StepScope
	public Tasklet errorHandling() {
		
		System.out.println("■errorHandling");
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				Map<String, Object>  stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
				String stepName = chunkContext.getStepContext().getStepName();
				if (stepExecutionContext.containsKey("qianfeng")) { // 第一次执行时，还没有这个键值对。执行else的处理
					System.out.println("■The second run will success....Step:" + stepName);
					return RepeatStatus.FINISHED;
				} else {
					System.out.println("■The first run will fail....Step:" + stepName);
					chunkContext.getStepContext().getStepExecution().getExecutionContext().put("qianfeng", true); //此时创建键值对。
					throw new RuntimeException("error...");
				}
			}
		};
	}

}
