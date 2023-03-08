package com.qianfeng.skiplistener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
public class MySkipListener implements SkipListener<String, String> {

	@Override
	public void onSkipInRead(Throwable t) {
		System.out.println("■onSkipInRead");
		
	}

	@Override
	public void onSkipInWrite(String item, Throwable t) {
		System.out.println("■onSkipInWrite");
	}

	@Override
	public void onSkipInProcess(String item, Throwable t) {
		System.out.println("■onSkipInProcess");
		System.out.println(item + "occur exception : " + t);
	}

}
