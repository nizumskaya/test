package ru.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerImpl implements Customer {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public int insertCustomer(String login, String password, DataSource dataSource, boolean rollback)
					throws SQLException {
		if (!TestHelper.verifyPhone(login))
			return ResponseErrorCodes.ERROR_2.getErrorCode();
		Connection con = null;
		PreparedStatement pst = null;
		String sql = "INSERT INTO customer(customer_login,customer_password) VALUES (?,?);";
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			pst = con.prepareStatement(sql);
			pst.setString(1, login);
			pst.setString(2, password);
			pst.executeUpdate();
			if (rollback) {
				con.rollback();
			} else {
				con.commit();
			}
		} catch (SQLException ex) {
			con.rollback();
			if (ex.getErrorCode() == 0)
				return ResponseErrorCodes.ERROR_1.getErrorCode();
			log.error("Error adding of customer. ", ex);
			return ResponseErrorCodes.ERROR_5.getErrorCode();
		} finally {
			if (null != pst)
				pst.close();
			if (null != con)
				con.close();
		}
		return ResponseErrorCodes.ERROR_0.getErrorCode();
	}

	@Override
	public BigDecimal getCustomerBalance(String login, DataSource dataSource) throws SQLException {
		Connection con = null;
		PreparedStatement pst = null;
		String sql = "SELECT balance.customer_balance FROM balance " + 
			"INNER JOIN customer ON customer.id=balance.customer_id " +
			"WHERE customer.customer_login=?";
		try {
			con = dataSource.getConnection();
			con.setAutoCommit(false);
			pst = con.prepareStatement(sql);
			pst.setString(1, login);
			ResultSet res = pst.executeQuery();
			res.next();
			return res.getBigDecimal(1);
		}catch(SQLException ex){
			con.rollback();
			log.error("Error getting customer balance. ", ex);
		}finally{
			if(null != pst) pst.close();
			if (null != con) con.close();
		}
		return new BigDecimal(0);
	}
}



