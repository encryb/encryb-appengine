package com.encryb.notify.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Login {
    @Id Long id;
    byte[] passwordHash;

    private Login() {}
    
    public Login(Long id, byte[] passwordHash) {
    	this.id = id;
    	this.passwordHash = passwordHash;
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
}