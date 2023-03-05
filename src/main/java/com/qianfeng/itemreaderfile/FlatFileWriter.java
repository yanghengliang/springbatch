package com.qianfeng.itemreaderfile;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Configuration;

@Configuration("flatFileWriter")
public class FlatFileWriter implements ItemWriter<Customer> {

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for (Customer customer : items) {
			System.out.println(customer);
		}
	}

}
