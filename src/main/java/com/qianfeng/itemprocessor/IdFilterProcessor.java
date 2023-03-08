package com.qianfeng.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * 
 * @ClassName: IdFilterProcessor
 * @Description: 处理2:根据ID判断是否输出。
 *　@author: yang
 * @date: 2023/03/08
 *
 */
@Component
public class IdFilterProcessor implements ItemProcessor<Customer, Customer>{

	@Override
	public Customer process(Customer item) throws Exception {
		if (item.getId()%2==0) {
			return item;
		} else {
			return null; //相当于把该对象过滤掉
		}
	}

}
