package ru.test;


public enum ResponseErrorCodes {
	
	ERROR_0(0, TestConstants.ErrorCodes.ERROR_0),
	ERROR_1(1, TestConstants.ErrorCodes.ERROR_1),
	ERROR_2(2, TestConstants.ErrorCodes.ERROR_2),
	ERROR_3(3, TestConstants.ErrorCodes.ERROR_3), 
	ERROR_5(5, TestConstants.ErrorCodes.ERROR_5);
	
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
