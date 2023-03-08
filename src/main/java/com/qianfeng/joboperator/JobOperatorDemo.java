package com.qianfeng.joboperator;

import java.util.Map;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 
 * @ClassName: JobOperatorDemo
 * @Description: 利用监听（StepExecutionListener）接收前台画面传来的参数。
 *　@author: yang
 * @date: 2023/03/09
 *
 */
@Configuration
public class JobOperatorDemo implements StepExecutionListener, ApplicationContextAware{
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private Map<String, JobParameter> parameter;
	
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private JobExplorer jobExplorer;
	@Autowired
	private JobRegistry jobRegistry;
	
	private ApplicationContext context;
	
	// 注册任务
	// 若是把下面的方法注释掉，会出下面的异常。方法名任意好像。
	// org.springframework.batch.core.launch.NoSuchJobException: No job configuration with the name [jobOperatorDemoJob] was registered
	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar123() throws Exception {
		JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
		postProcessor.setJobRegistry(jobRegistry);
		postProcessor.setBeanFactory(context.getAutowireCapableBeanFactory());
		postProcessor.afterPropertiesSet();
		
		return postProcessor;
		
	}
	
	// 注意需要自己生成Bean，不能由框架来自动注入!!!
	@Bean
	public JobOperator jobOperator() {
		SimpleJobOperator operator = new SimpleJobOperator();
		operator.setJobLauncher(jobLauncher); //1
		operator.setJobParametersConverter(new DefaultJobParametersConverter()); //2
		operator.setJobRepository(jobRepository);//3
		operator.setJobExplorer(jobExplorer);//4 获取和任务相关的信息，比如Job实例
		operator.setJobRegistry(jobRegistry);//5 注册jobOperatorDemoJob
		return operator;
		
		
	}
	
	@Bean
	public Job jobOperatorDemoJob() {
		return jobBuilderFactory.get("jobOperatorDemoJob")
				.start(jobOperatorDemoStep())
				.build();
	}

	@Bean
	public Step jobOperatorDemoStep() {
		return stepBuilderFactory.get("jobOperatorDemoStep")
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

	// 通过实现ApplicationContextAware接口，实现context的自动注入
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
}
