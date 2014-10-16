package com.encryb.notify.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class AcceptInvite {
	
	@Id
	private Long invitee;
	@Parent
	private Key<User> inviter;
	@Load	
	private Long date;
	private String datastoreId;
	
	public AcceptInvite() {}
	
	public AcceptInvite(Key<User> inviter, Long invitee, long date, String datastoreId) {
		this.invitee = invitee;
		this.inviter = inviter;
		this.date = date;
		this.datastoreId = datastoreId;
	}
	
	
	
	public long getInvitee() {
		return invitee;
	}
	public void setInvitee(long invitee) {
		this.invitee = invitee;
	}
	public Key<User> getInviter() {
		return inviter;
	}
	public void setInviter(Key<User> inviter) {
		this.inviter = inviter;
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
