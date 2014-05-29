package ru.test;

import java.io.Serializable;

public class TestRequest implements Serializable {

	private static final long serialVersionUID = -877042686751004831L;
	
	private String login;
	private String password;
	private RequestType request_type;

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public RequestType getRequest_type() {
		return request_type;
	}
	public void setRequest_type(RequestType request_type) {
		this.request_type = request_type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((request_type == null) ? 0 : request_type.hashCode());
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
		TestRequest other = (TestRequest) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (request_type != other.request_type)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Request [login=" + login + ", password=" + password + ", request_type=" + request_type + "]";
	}
}
