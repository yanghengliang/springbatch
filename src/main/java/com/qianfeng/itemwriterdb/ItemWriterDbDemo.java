package com.qianfeng.itemwriterdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @ClassName: ItemWriterDbDemo
 * @Description: 从文件中读取数据，写入到数据库。
 *　@author: yang
 * @date: 2023/03/06
 *
 */
@Configuration
public class ItemWriterDbDemo {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	@Qualifier("flatFileReader")
	private ItemReader<Customer> flatFileReader;
	@Autowired
	@Qualifier("itemWriterDb")
	private ItemWriter<? super Customer> itemWriterDb;
	
	@Bean
	public Job itemWriterDbDemoJob() {
		return jobBuilderFactory.get("itemWriterDbDemoJob")
				.start(itemWriterDbDemoStep())
				.build();
		
	}

	@Bean
	public Step itemWriterDbDemoStep() {
		return stepBuilderFactory.get("itemWriterDbDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(flatFileReader)
				.writer(itemWriterDb)
				.build();
	}
	

}
