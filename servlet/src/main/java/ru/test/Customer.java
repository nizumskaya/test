package ru.test;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.sql.DataSource;


public interface Customer {
	
	public int insertCustomer(String login, String password, DataSource dataSource, boolean rollback) throws SQLException;
	
	public BigDecimal getCustomerBalance(String login, DataSource dataSource) throws SQLException;

}
