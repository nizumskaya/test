package ru.test;

public enum RequestType {
	
		NEWAGT("new-agt"),
		AGTBAL("agt-bal");

	private final String name;

	
	public String getName() {
		return name;
	}

	RequestType(String name){
		this.name=name;
	}
}


