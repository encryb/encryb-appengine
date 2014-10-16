package com.encryb.notify.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Invite {
	
	@Id
	private Long inviter;	
	@Parent
	private Key<User> invitee;
	private Long date;
	private String datastoreId;
	
	public Invite() {}
	
	public Invite(Key<User> invitee, long inviter, long date, String datastoreId) {
		this.invitee = invitee;
		this.setInviter(inviter);
		this.date = date;
		this.datastoreId = datastoreId;
	}
	
	
	public Long getInviter() {
		return inviter;
	}

	public void setInviter(Long inviter) {
		this.inviter = inviter;
	}

	public Key<User> getInvitee() {
		return invitee;
	}
	public void setInvitee(Key<User> invitee) {
		this.invitee = invitee;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public String getDatastoreId() {
		return datastoreId;
	}
	public void setDatastoreId(String datastoreId) {
		this.datastoreId = datastoreId;
	}
}
