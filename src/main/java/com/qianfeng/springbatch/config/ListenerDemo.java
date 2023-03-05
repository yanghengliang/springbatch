package com.qianfeng.springbatch.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.qianfeng.springbatch.listener.MyChunkListener;
import com.qianfeng.springbatch.listener.MyJobListener;
/**
 * 
 * @ClassName: ListenerDemo
 * @Description: TODO
 *　@author: yang
 * @date: 2023/02/26
 *
 */
@Configuration
@EnableBatchProcessing
public class ListenerDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job listenerJob() {
		return jobBuilderFactory.get("listenerJob")
				.start(listenerJobstep1())
				.listener(new MyJobListener())
				.build();

	}
	
	@Bean
	public Step listenerJobstep1() {
		return stepBuilderFactory.get("listenerJobstep1")
				.<String,String>chunk(2)  // reade, process, write
				.faultTolerant()
				.listener(new MyChunkListener())
				.reader(read())
				.writer(write())
				.build();
	}


	@Bean
	public ItemReader<String> read() {
		return new ListItemReader<String>(Arrays.asList("java", "spring", "mybatis", ""));
	}
	
	@Bean
	public ItemWriter<String> write() {
		
		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {
				for (String item : items) {
					System.out.println(item);
				}
			}
			
		};
	}

}
