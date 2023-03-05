package com.qianfeng.restart;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;


@Component("restartReader")
public class RestartReader implements ItemStreamReader<Customer>{
	
	private FlatFileItemReader<Customer> customerFlatFileItemReader = new FlatFileItemReader<Customer>();
	private Long curLine = 0L;
	private boolean restart = false;
	private ExecutionContext executionContext; //用来向数据库存储相关信息
	
	public RestartReader() {
		customerFlatFileItemReader.setResource(new ClassPathResource("restart.txt"));
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		//制定四个表头字段
		tokenizer.setNames(new String[] {"id","firstName","lastName","birthday"});
	    //把解析出的一行数据映射为Customer对象
	    DefaultLineMapper<Customer> mapper = new DefaultLineMapper<Customer>();
	    mapper.setLineTokenizer(tokenizer);
	    mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
			
			@Override
			public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
				Customer customer = new Customer();
				customer.setId(fieldSet.readLong("id"));
				customer.setFirstName(fieldSet.readString("firstName"));
				customer.setLastName(fieldSet.readString("lastName"));
				customer.setBirthday(fieldSet.readString("birthday"));
				return customer;
			}
		});
	    mapper.afterPropertiesSet();
	    customerFlatFileItemReader.setLineMapper(mapper);
	}

	//step开始执行之前，执行此方法。
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.executionContext = executionContext;
		if (executionContext.containsKey("curLine")) { //在step执行之前判断是否有curline这个关键字，此时因还没有执行，所以没有。
			this.curLine = executionContext.getLong("curLine");
			this.restart = true;
		} else {
			this.curLine = 0L;
			executionContext.put("curLine", this.curLine);
			System.out.println("open()_Start reading from line: " + this.curLine + 1);
		}
	}

	// 执行完一个chunk之后，执行此方法。一旦发生异常，就不再执行update方法
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.put("curLine", this.curLine); //把当前行数存储到数据库中。重新启动时会从当前行数开始读。
		System.out.println("current Line: " + this.curLine);
		
	}

	// Step执行后，立刻执行此方法。
	@Override
	public void close() throws ItemStreamException {
		System.out.println("■close()");
	}

	@Override
	public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Customer customer = null;
		this.curLine++;
		
		if (restart) {
			customerFlatFileItemReader.setLinesToSkip(this.curLine.intValue() -1);
			restart = false;
			System.out.println("read()_Start reading from line: " + this.curLine);
		}
		
		customerFlatFileItemReader.open(this.executionContext);
		customer = customerFlatFileItemReader.read();
		
		if (customer != null && customer.getFirstName().equals("wrongname")) {
			throw new RuntimeException("Something wrong. Customer id : " + customer.getId());
		}
		return customer;
	}

}
