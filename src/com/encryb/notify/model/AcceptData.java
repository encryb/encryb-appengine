package com.encryb.notify.model;

public class AcceptData {
	
	Long date;
	Long userId;
	String datastoreId;
	
	public AcceptData(AcceptInvite accept) {
		
		date = accept.getDate();
		userId = accept.getInvitee();
		datastoreId = accept.getDatastoreId();		
	}

	public Long getDate() {
		return date;
	}

	public Long getUserId() {
		return userId;
	}

	public String getDatastoreId() {
		return datastoreId;
	}

}
