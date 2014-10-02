package com.edible.entity;

import java.io.Serializable;

/**
 * 账号类，记录用户的账号信息，包括邮箱，密码以及详细的用户信息
 * @author mingjiang
 *
 */
public class Account implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private User user;

	
	public Account() {}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Account{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				", user=" + user +
				'}';
	}
}
