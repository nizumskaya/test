package ru.test;

import java.util.regex.Pattern;

public class TestHelper {
	
	public static boolean verifyPhone(String phone) {
		String pattern = "^[0-9]{10}$";
		return Pattern.matches(pattern, phone);
	}

	public static int getPasswordHash(String customer_login, String customer_password) {
		return customer_password.hashCode() + customer_login.hashCode();
	}
}
