package ru.test;

public enum RequestType {

		NEWAGT(TestConstants.XMLRequestType.NEWAGT),
		AGTBAL(TestConstants.XMLRequestType.AGTBAL);

	private final String name;

	
	public String getName() {
		return name;
	}

	RequestType(String name){
		this.name=name;
	}
	
	
}


