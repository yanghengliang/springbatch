package com.qianfeng.retry;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryDemo {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> retryItemWriter;
	
	@Autowired
	private ItemProcessor<String, String> retryItemProcessor;
	
	@Bean
	public Job retryDemoJob() {
		return jobBuilderFactory.get("retryDemoJob")
				.start(retryDemoStep())
				.build();
	}

	// Retry策略的步骤1,2,3
	@Bean
	public Step retryDemoStep() {
		return stepBuilderFactory.get("retryDemoStep")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(retryItemProcessor)
				.writer(retryItemWriter)
				.faultTolerant() // 1 容错
				.retry(CustomRetryException.class) // 2指定容什么错。也就是说，发生这个异常时，不要让任务停下来，而是重新尝试。
				.retryLimit(5) //3指定允许重试的次数。 注意此处: 读处理，处理，写处理总的异常次数不要超过5
				.build();
	}

	@Bean
	@StepScope
	public ItemReader<? extends String> reader() {
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			items.add(String.valueOf(i));
		}
		ListItemReader<String> reader = new ListItemReader<String>(items);
		return reader;
	}
	

}
