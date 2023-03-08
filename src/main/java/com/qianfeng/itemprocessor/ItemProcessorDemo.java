package com.qianfeng.itemprocessor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemProcessorDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcReader")
	private ItemReader<? extends Customer> dbJdbcReader;
	
	
	@Autowired
	@Qualifier("dbFileWriter")
	private ItemWriter<? super Customer> dbFileWriter;

	@Autowired
	private ItemProcessor<Customer, Customer> firstNameUpperProcessor;
	
	@Autowired
	private  ItemProcessor<Customer, Customer> idFilterProcessor;
	
	@Bean
	public Job itemProcessorDemoJob() {
		
		return jobBuilderFactory.get("itemProcessorDemoJob")
				.start(itemProcessorDemoStep())
				.build();
		
	}

	@Bean
	public Step itemProcessorDemoStep() {
		return stepBuilderFactory.get("itemProcessorDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(dbJdbcReader)
//				.processor(firstNameUpperProcessor)  //只有一个处理是这么写
				.processor(process())
				.writer(dbFileWriter)
				.build();
	}
	
	// 多个处理的时候这么写
	@Bean
	public CompositeItemProcessor<Customer, Customer> process() {
		CompositeItemProcessor<Customer, Customer> processor = new CompositeItemProcessor<Customer, Customer>();
		
		List<ItemProcessor<Customer, Customer>> delegates = new ArrayList<ItemProcessor<Customer,Customer>>();
		delegates.add(firstNameUpperProcessor);
		delegates.add(idFilterProcessor);
		processor.setDelegates(delegates);
		
		return processor;
	}
	
	
	
}
