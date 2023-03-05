package com.qianfeng.itemwriterxml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
public class XmlItemWriterConfig {
	
	@Bean(name = "xmlItemWriter")
	public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {
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
		Path path = Paths.get("D:\\2023\\springbatch\\db_2_xml_customer.xml");
		writer.setResource(new FileSystemResource(path));
		
	    // 返回writer对象	
		return writer;
	}

}
