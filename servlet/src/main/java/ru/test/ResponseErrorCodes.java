package ru.test;


public enum ResponseErrorCodes {
	
	ERROR_0(0, "все хорошо"),
	ERROR_1(1, "такой агент уже  зарегистрирован"),
	ERROR_2(2, "неверный формат телефона"),
	ERROR_3(3, "плохой пароль"), 
	ERROR_5(5, "другая ошибка повторите позже");
	
	private int errorCode;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public int getErrorCode() {
		return errorCode;
	}

	ResponseErrorCodes(int errorCode, String description){
		this.description=description;
		this.errorCode=errorCode;
	}
}
