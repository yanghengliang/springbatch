package com.qianfeng.itemwriterdb;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;


@Configuration
public class FlatFileReaderConfig {

	@Bean
	public FlatFileItemReader<Customer> flatFileReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<Customer>();
		
		reader.setResource(new ClassPathResource("customers.txt"));
//		reader.setLinesToSkip(1);// 跳过第一行表头
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
