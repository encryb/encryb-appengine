package com.encryb.notify.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Login {
    @Id Long id;
    byte[] passwordHash;
    String bcryptHash;

    private Login() {}
    
    public Login(Long id, String bcryptHash) {
    	this.id = id;
    	this.bcryptHash = bcryptHash;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(byte[] passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getBcryptHash() {
		return bcryptHash;
	}

	public void setBcryptHash(String bcryptHash) {
		this.bcryptHash = bcryptHash;
	}
}