package ru.test;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class <code>Response</code> . 
 * <p>
 * <b>Copyright: </b>Copyright (c)2014
 * </p>
 * @author izumskaya <br/>
 * <b>e-mail</b>: izumskaya@mail.ru 
 */
public class TestResponse implements Serializable {
	

	private static final long serialVersionUID = -8665639158237229132L;
	
	private String resultCode;
	private BigDecimal balance;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((resultCode == null) ? 0 : resultCode.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestResponse other = (TestResponse) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (resultCode == null) {
			if (other.resultCode != null)
				return false;
		} else if (!resultCode.equals(other.resultCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Response [resultCode=" + resultCode + ", balance=" + balance + "]";
	}
}
