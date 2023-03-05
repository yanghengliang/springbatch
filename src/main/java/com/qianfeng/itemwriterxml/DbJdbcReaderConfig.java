package com.qianfeng.itemwriterxml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
@Configuration
public class DbJdbcReaderConfig {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcPagingItemReader<Customer> dbJdbcReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(10); //每次取10个
		
		reader.setRowMapper(new RowMapper<Customer>() {
			
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Customer customer = new Customer();
				customer.setId(rs.getLong(1));
				customer.setFirstName(rs.getString(2));
				customer.setLastName(rs.getString(3));
				customer.setBirthday(rs.getString(4));
				return customer;
			}
		});
		
		//指定SQL文
		MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
		provider.setSelectClause("id,firstName,lastName,birthday");
		provider.setFromClause("from customer");
		//指定用来排序的字段
		Map<String, Order> sort = new HashMap<String, Order>(1);
		sort.put("id",Order.ASCENDING);
		provider.setSortKeys(sort);
		reader.setQueryProvider(provider);
		
		return reader;
	}
}
