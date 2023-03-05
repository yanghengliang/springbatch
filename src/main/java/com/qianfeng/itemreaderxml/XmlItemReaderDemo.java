package com.qianfeng.itemreaderxml;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class XmlItemReaderDemo {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ItemWriter<? super Customer> xmlFileWriter;
	
	@Bean
	public Job xmlItemReaderDemoJob() {
		return jobBuilderFactory.get("xmlItemReaderDemoJob")
				.start(xmlItemReaderDemoStep()).build();
	}

	@Bean
	public Step xmlItemReaderDemoStep() {
		return stepBuilderFactory.get("xmlItemReaderDemoStep")
				.<Customer,Customer>chunk(2)
				.reader(xmlFileReader())
				.writer(xmlFileWriter)
				.build();
	}

	@Bean
	@StepScope
	public StaxEventItemReader<Customer> xmlFileReader() {
		StaxEventItemReader<Customer> staxEventItemReader = new StaxEventItemReader<Customer>();
		staxEventItemReader.setResource(new ClassPathResource("customer.xml"));
		
		//指定需要处理的跟标签
		staxEventItemReader.setFragmentRootElementName("customer");
		//把xml转成对象
		XStreamMarshaller unmarshaller = new XStreamMarshaller();
		Map<String, Class> map = new HashMap<String, Class>();
		map.put("customer", Customer.class);
		unmarshaller.setAliases(map);
		
		staxEventItemReader.setUnmarshaller(unmarshaller);
		return staxEventItemReader;
	}

}
