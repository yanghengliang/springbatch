package com.qianfeng.itemwriterfile;

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
 * @ClassName: FileItemWriterDemo
 * @Description: 从数据库读取数据，写入普通文件
 *　@author: yang
 * @date: 2023/03/05
 *
 */
@Configuration
public class FileItemWriterDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	@Qualifier("dbJdbcReader")
	private ItemReader<Customer> dbJdbcReader;
	@Autowired
	@Qualifier("fileItemWriter")
	private ItemWriter<Customer> fileItemWriter;
	
	@Bean
	public Job fileItemWriterDemoJob2() {
		return jobBuilderFactory.get("fileItemWriterDemoJob2")
				.start(fileItemWriterDemoStep())
				.build();
	}

	@Bean
	public Step fileItemWriterDemoStep() {
		return stepBuilderFactory.get("fileItemWriterDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(dbJdbcReader)
				.writer(fileItemWriter)
				.build();
	}
}
