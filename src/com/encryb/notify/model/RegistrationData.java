package com.encryb.notify.model;

public class RegistrationData {
	
	Long userId;
	String password;
	
	public RegistrationData(Long userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	public Long getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

}
