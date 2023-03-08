package com.qianfeng.skiplistener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
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
public class SkipListenerDemo {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ItemWriter<String> skipItemWriter;
	
	@Autowired
	private ItemProcessor<String, String> skipItemProcessor;

	@Autowired
	private SkipListener<String, String> mySkipListener;
	
	@Bean
	public Job skipListenerDemoJob() {
		return jobBuilderFactory.get("skipListenerDemoJob")
				.start(skipListenerDemoStep1())
				.build();
	}

	@Bean
	public Step skipListenerDemoStep1() {
		return stepBuilderFactory.get("skipListenerDemoStep1")
				.<String,String>chunk(10)
				.reader(reader())
				.processor(skipItemProcessor)
				.writer(skipItemWriter)
				.faultTolerant() //1 容错
				.skip(CustomRetryException.class)//2指定跳过异常
				.skipLimit(5) //3指定跳过次数
				.listener(mySkipListener) //4监听
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
