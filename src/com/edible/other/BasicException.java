package com.edible.other;

public class BasicException extends RuntimeException{
	private int code;
	private String msg;
	
	public BasicException(String message){
		super(message);
		msg = message;
		code = Status.SYSTEM_ERROR.getStatusCode();
	}
	public BasicException(int msgCode, String message){
		super(message);
		code = msgCode;
		msg = message;
	}
}
