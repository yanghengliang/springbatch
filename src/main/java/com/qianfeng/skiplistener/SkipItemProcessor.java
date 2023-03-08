package com.qianfeng.skiplistener;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SkipItemProcessor implements ItemProcessor<String, String>{
	private int attemptCount = 0;

	@Override
	public String process(String item) throws Exception {
		
		System.out.println("■procession item : " + item);
		
		if (item.equalsIgnoreCase("26")) {
			attemptCount++;
			if (attemptCount >= 3) {
				System.out.println("■Retried " + attemptCount + "times success.");
				return String.valueOf(Integer.valueOf(item) * -1);
			} else {
				System.out.println("■Processed the " + attemptCount + "times fail.");
				throw new CustomRetryException("Process faild. Attept : " + attemptCount);
			}
		} else {
			return String.valueOf(Integer.valueOf(item)* -1);
		}
	}
	
}
