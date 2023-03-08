package com.qianfeng.multifile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class MultiFileItemWriterConfig {

	// 以Json的形式输出到普通文件
	@Bean
	public FlatFileItemWriter<Customer> jsonItemWriter() throws Exception {
		System.out.println("■jsonItemWriter() ");
		// 把Customer对象转换成字符串输出到文件
		FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
		
//		String path = "‪D:\\2023\\springbatch\\customer.txt";
		Path path = Paths.get("D:\\2023\\springbatch\\customer2.txt");
		
		writer.setResource(new FileSystemResource(path));
		
		//把一个Customer对象转成一行字符串
		writer.setLineAggregator(new LineAggregator<Customer>() {
			ObjectMapper mapper = new ObjectMapper();
			@Override
			public String aggregate(Customer item) {
				String str = null;
				try {
					str = mapper.writeValueAsString(item);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				return str;
			}
		});
		
		writer.afterPropertiesSet();
		
		return writer;
	}

	// 输出到xml文件
	@Bean
	public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {
		System.out.println("■xmlItemWriter() ");
		// 把Customer对象转换成字符串输出到文件
		StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<Customer>();
		
		//把对象转成xml
		XStreamMarshaller marshaller = new XStreamMarshaller();
		Map<String, Class<Customer>> aliases = new HashMap<String, Class<Customer>>();
		aliases.put("customer", Customer.class);
		marshaller.setAliases(aliases);
		
		//设置根标签
		writer.setRootTagName("customers");
		//设置根标签的子标签，即详细内容
		writer.setMarshaller(marshaller);
		
		//设置文件路径
		Path path = Paths.get("D:\\2023\\springbatch\\db_2_xml_customer2.xml");
		writer.setResource(new FileSystemResource(path));
		
	    // 返回writer对象	
		return writer;
	}
	
	// 利用前面的两种方式，进行多文件输出(不分类)。
//	@Bean
//	public CompositeItemWriter<Customer> multiFileItemWriter() throws Exception{
//		CompositeItemWriter<Customer> writer = new CompositeItemWriter<Customer>();
//		
//		//设置委托
//		writer.setDelegates(Arrays.asList(jsonItemWriter(), xmlItemWriter()));
//		
//		writer.afterPropertiesSet();
//		
//		return writer;
//	}
	
	@Bean
	public ClassifierCompositeItemWriter<Customer> multiFileItemWriter(){
		ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<Customer>();
		writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
			
			@Override
			public ItemWriter<? super Customer> classify(Customer customer) {
				ItemWriter<Customer> writer = null;
				//按Customer的ID进行分类
				try {
					writer = customer.getId()%2==0?jsonItemWriter():xmlItemWriter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return writer;
			}
		});
		return writer;
	}
	
	
	
}
