package com.qianfeng.itemwriterfile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {

	public static void main(String[] args) {
		Path path = Paths.get("D:\\2023\\springbatch\\customer.txt");
		System.out.println(path.toString());
	}

}
