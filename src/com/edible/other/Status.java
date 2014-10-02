package com.edible.other;

/**
 * Created by mingjiang on 9/22/14.
 */
public enum Status {
	SYSTEM_ERROR(-1, "SYSTEM_ERROR"),
	SUCCESS(0,"SUCCESS"),
	ACCOUNT_REGISTERED(1, "ACCOUNT_REGISTERED"),
	ACCOUNT_NOT_FOUND(2, "ACCOUNT_NOT_FOUND"),
	PASSWORD_WRONG(3, "PASSWORD_WRONG"),
	USER_NOT_FOUND(4,"USER_NOT_FOUND"),
	FOOD_NOT_FOUND(5, "FOOD_NOT_FOUND"),
	IMAGE_NOT_FOUND(6, "IMAGE_NOT_FOUND"),
	REVIEW_NOT_FOUND(7, "REVIEW_NOT_FOUND"),
	LANGUAGE_NOT_FOUND(8, "LANGUAGE_NOT_FOUND"),
	ILLEGAL_REQUEST(9, "ILLEGAL_REQUEST"),
	DISCOUNT_NOT_FOUND(10, "DISCOUNT_NOT_FOUND"),
	RESTAURANT_NOT_FOUND(11, "RESTAURANT_NOT_FOUND");



	private int statusCode;
	private String statusMessage;


	public int getStatusCode(){
		return this.statusCode;
	}

	public String getStatusMessage(){
		return this.statusMessage;
	}

	private Status(int statusCode, String statusMessage){
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}
}
