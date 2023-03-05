package com.qianfeng.itemreaderfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * 
 * @ClassName: FileItemReaderDemo
 * @Description: 从文件中读数据
 *　@author: yang
 * @date: 2023/03/01
 *
 */
@Configuration
public class FileItemReaderDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	@Qualifier("flatFileWriter")
	private ItemWriter<? super Customer> flatFileWriter;
	
	@Bean
	public Job fileItemReaderDemoJob() {
		return jobBuilderFactory.get("fileItemReaderDemoJob")
				.start(fileItemReaderDemoStep())
				.build();
	}

	@Bean
	public Step fileItemReaderDemoStep() {
		return stepBuilderFactory.get("fileItemReaderDemoStep")
				.<Customer,Customer>chunk(2)
				.reader(flatFileReader())
				.writer(flatFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		
		reader.setResource(new ClassPathResource("customer.txt"));
		reader.setLinesToSkip(1);// 跳过第一行表头
		//从第二行开始数据解析
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"id","firstName","lastName","birthday"});
	    //把解析出的一行数据映射为Customer对象
	    DefaultLineMapper<Customer> mapper = new DefaultLineMapper<Customer>();
	    mapper.setLineTokenizer(tokenizer);
	    mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstName"));
				customer.setLastName(fieldSet.readString("lastName"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
	    mapper.afterPropertiesSet();
	    reader.setLineMapper(mapper);
		return reader;
	}
}
