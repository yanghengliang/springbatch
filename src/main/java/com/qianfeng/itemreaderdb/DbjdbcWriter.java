package com.qianfeng.itemreaderdb;

import java.util.List;


import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component("dbJdbcWriter")
public class DbjdbcWriter implements ItemWriter<User>{

	@Override
	public void write(List<? extends User> items) throws Exception {
		for (User user : items) {
			System.out.println(user);
		}
	}

}
