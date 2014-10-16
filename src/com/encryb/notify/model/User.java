package com.encryb.notify.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User {
    @Id Long id;
    String name;
    String intro;
    String pictureUrl;
    String publicKey;

    private User() {}
    
    public User(Long id, String name, String intro, String pictureUrl, String publicKey) {
    	this.id = id;
    	this.name = name;
    	this.intro = intro;
    	this.pictureUrl = pictureUrl;
    	this.publicKey = publicKey;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
}