package com.qianfeng.itemreaderxml;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class XmlFileWriter implements ItemWriter<Customer>{

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for (Customer customer : items) {
			System.out.println(customer);
		}
		
	}

}
