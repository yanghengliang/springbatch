package com.qianfeng.itemreaderdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @ClassName: ItemReaderDbDemo
 * @Description: 从数据库读数据
 *　@author: yang
 * @date: 2023/03/01
 *
 */
@Configuration
public class ItemReaderDbDemo {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	@Qualifier("dbJdbcWriter")
	private ItemWriter<? super User> dbJdbcWriter;
	
	@Bean
	public Job itemReaderDbJob() {
		return jobBuilderFactory.get("itemReaderDbJob")
				.start(itemReaderDbStep()).build();
	}

	@Bean
	public Step itemReaderDbStep() {
		
		return stepBuilderFactory.get("itemReaderDbStep")
				.<User,User>chunk(2)
				.reader(dbJdbcReader())
				.writer(dbJdbcWriter)
				.build();
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<User> dbJdbcReader() {
		JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(2);
		
		reader.setRowMapper(new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setAge(rs.getInt(4));
				return user;
			}
		});
		
		//指定SQL文
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("id,username,password,age");
		provider.setFromClause("from USER");
		//指定用来排序的字段
		Map<String, Order> sort = new HashMap<String, Order>(1);
		sort.put("id",Order.ASCENDING);
		provider.setSortKeys(sort);
		reader.setQueryProvider(provider);
		
		
		
		return reader;
	}
	
}
