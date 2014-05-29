package ru.test;

public class TestConstants {
	
	public interface Customer{
		public static final String PHONE_PATTERN = "^[0-9]{10}$";
	}
	
	public interface Messages{
		public static final String ERROR_GETTING_CUSTOMER_BALANCE = "Error getting customer balance. ";
	}
	
	public interface ErrorCodes{
		public static final String ERROR_0 = "все хорошо";
		public static final String ERROR_1 = "такой агент уже  зарегистрирован";
		public static final String ERROR_2 = "неверный формат телефона";
		public static final String ERROR_3 = "плохой пароль";
		public static final String ERROR_5 = "другая ошибка повторите позже";
	}
	
	public interface XMLRequestType{
		public static final String NEWAGT = "new-agt";
		public static final String AGTBAL = "agt-bal";
	}
	
	public interface ExceptionMessages{
		public static final String ERROR_WRITING_INTO_HTTP_RESPONSE = "Error writing into HTTPResponse. ";
		public static final String ERROR_ADDING_OF_CUSTOMER = "Error adding of customer. ";
		public static final String ERROR_SQL_EXECUTION = "Ошибка выполения SQL запроса. ";
		public static final String ERROR_CONFIGURATION = "Ошибка конфигурации. ";
		public static final String ERROR_PARSING = "Ошибка парсинга. ";
		public static final String ERROR_TRANSFORMATION_DATA = "Ошибка трансформации данных. ";
	}
	
	public interface XMLParameters{
		public static final String RESULT_CODE = "result-code";
		public static final String BALANCE = "bal";
		public static final String RESPONSE_NAME = "response";
		public static final String PASSWORD = "password";
		public static final String LOGIN = "login";
		public static final String REQUEST_TYPE = "request-type";
		public static final String REQUEST_NAME = "request";
	}
}
