package com.qianfeng.itemprocessor;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class DbFileWriterConfig {
	
	@Bean
	public FlatFileItemWriter<Customer> dbFileWriter() throws Exception {
		// 把Customer对象转换成字符串输出到文件
		FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
		
//		String path = "‪D:\\2023\\springbatch\\customer.txt";
		Path path = Paths.get("D:\\2023\\springbatch\\customer3.txt");
		
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
	

}
