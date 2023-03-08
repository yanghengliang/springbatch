package com.qianfeng.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: FirstNameUpperProcessor
 * @Description: 处理1:对名字的姓进行字母大写转换
 *　@author: yang
 * @date: 2023/03/08
 *
 */
@Component
public class FirstNameUpperProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		Customer customer = new Customer();
		customer.setId(item.getId());
		customer.setFirstName(item.getFirstName().toUpperCase());
		customer.setLastName(item.getLastName());
		customer.setBirthday(item.getBirthday());
		return customer;
	}

}
