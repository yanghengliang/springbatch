package com.qianfeng.joblauncher;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mysql.cj.x.protobuf.MysqlxNotice.SessionStateChanged.Parameter;

/**
 * 
 * @ClassName: JobLauncherDemo
 * @Description: 利用监听（StepExecutionListener）接收前台画面传来的参数。
 *　@author: yang
 * @date: 2023/03/09
 *
 */
@Configuration
public class JobLauncherDemo implements StepExecutionListener{
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private Map<String, JobParameter> parameter;
	
	@Bean
	public Job jobLauncherDemoJob() {
		return jobBuilderFactory.get("jobLauncherDemoJob")
				.start(jobLauncherDemoStep())
				.build();
	}

	@Bean
	public Step jobLauncherDemoStep() {
		return stepBuilderFactory.get("jobLauncherDemoStep")
				.listener(this) // 1 使用监听器
				.tasklet(new Tasklet() {
					
					@Override
					public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
						
						//3输出获取到的参数
						System.out.println(parameter.get("msg").getValue());
						
						return RepeatStatus.FINISHED;
					}
				}).build();
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		parameter = stepExecution.getJobParameters().getParameters(); //2从Job中获取参数
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return null;
	}
	
}
