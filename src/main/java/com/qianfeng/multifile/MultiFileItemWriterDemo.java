package com.qianfeng.multifile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @ClassName: MultiFileItemWriterDemo
 * @Description: 从数据库中读取数据，并写入多个文件
 *　@author: yang
 * @date: 2023/03/06
 *
 */
@Configuration
public class MultiFileItemWriterDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("dbJdbcReader")
	private ItemReader<? extends Customer> dbJdbcReader;
	@Autowired
	@Qualifier("multiFileItemWriter")
	private ItemWriter<? super Customer> multiFileItemWriter;
	
	
	//若是使用分类输出的话，需要注入下面两个Writer
	@Autowired
	@Qualifier("jsonItemWriter")
	private ItemStreamWriter<Customer> jsonItemWriter;  //类型是子类的ItemStreamWriter 而不是ItemWriter
	@Autowired
	@Qualifier("xmlItemWriter")
	private ItemStreamWriter<Customer> xmlItemWriter; //类型是子类的ItemStreamWriter 而不是ItemWriter
	
	@Bean
	public Job multiFileItemWriterDemoJob2() {
		return jobBuilderFactory.get("multiFileItemWriterDemoJob2").start(multiFileItemWriterDemoStep()).build();
	}

	// CompositeItemWriterの場合
//	@Bean
//	public Step multiFileItemWriterDemoStep() {
//		return stepBuilderFactory.get("multiFileItemWriterDemoStep")
//				.<Customer,Customer>chunk(10)
//				.reader(dbJdbcReader)
//				.writer(multiFileItemWriter)
//				.build();
//	}
	
	// ClassifierCompositeItemWriterの場合
	@Bean
	public Step multiFileItemWriterDemoStep() {
		return stepBuilderFactory.get("multiFileItemWriterDemoStep")
				.<Customer,Customer>chunk(10)
				.reader(dbJdbcReader)
				.writer(multiFileItemWriter)
				.stream(jsonItemWriter) //类型是子类的ItemStreamWriter 而不是ItemWriter
				.stream(xmlItemWriter)  //类型是子类的ItemStreamWriter 而不是ItemWriter
				.build();
	}
}
