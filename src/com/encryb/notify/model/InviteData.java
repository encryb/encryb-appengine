package com.encryb.notify.model;


public class InviteData {
	
	Long date;
	Long userId;
	String name;
	String intro;
	String pictureUrl;
	String publicKey;
	String datastoreId;
	
	public InviteData(Invite invite, User inviter) {
		date = invite.getDate();
		
		userId = inviter.getId();
		name = inviter.getName();
		intro = inviter.getIntro();
		pictureUrl = inviter.getPictureUrl();
		publicKey = inviter.getPublicKey();
		
		datastoreId = invite.getDatastoreId();		
	}

	public Long getDate() {
		return date;
	}

	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}
	
	public String getIntro() {
		return intro;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getDatastoreId() {
		return datastoreId;
	}

}
