package com.encryb.notify.service;

import static com.encryb.notify.OfyService.ofy;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import com.encryb.notify.model.AcceptInvite;
import com.encryb.notify.model.Invite;
import com.encryb.notify.model.AcceptData;
import com.encryb.notify.model.InviteData;
import com.encryb.notify.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.googlecode.objectify.Key;

@Api(name = "encrybuser", version = "v1")
public class UserService {
	
    @ApiMethod(name = "getProfile")
    public User getProfile(@Named("id") Long id) {
    	User user = ofy().load().type(User.class).id(id).now();
    	return user;
    }
	
    @ApiMethod(name = "setProfile", httpMethod = HttpMethod.PUT)
    public void setProfile(@Named("id") Long id,
    					   @Named("name") String name,
    					   @Named("intro") String intro,
    					   @Named("pictureUrl") String pictureUrl,
    					   @Named("publicKey") String publicKey) {

    	User user = new User(id, name, intro, pictureUrl, publicKey);
    	ofy().save().entity(user);
    }
    
    @ApiMethod(name = "invite")
    public void invite(@Named("id") Long id,
    				   @Named("inviteeId") Long inviteeId,
    				   @Named("datastoreId") String datastoreId) {
    	Key<User> inviteeKey = Key.create(User.class, inviteeId);
    	
    	
    	Invite invite = new Invite(inviteeKey, id, 
    			System.currentTimeMillis() / 1000,
    			datastoreId);
    	
    	ofy().save().entity(invite);
    }
    
    @ApiMethod(name = "acceptInvite")
    public void acceptInvite(@Named("id") Long id, 
    						 @Named("inviterId") Long inviter,
    						 @Named("datastoreId") String datastoreId) {
    	Key<User> inviterKey = Key.create(User.class, inviter);
    	
    	AcceptInvite accept = new AcceptInvite(inviterKey, id, 
    			System.currentTimeMillis() / 1000, 
    			datastoreId);
    	ofy().save().entity(accept);
    }
    
    @ApiMethod(name = "getInvites")
    public List<InviteData> getInvites(@Named("id") Long id) {
    	
    	List<InviteData> response = new LinkedList<InviteData>();
    	Key<User> idKey = Key.create(User.class, id);

    	List<Invite> invites = ofy().load().type(Invite.class).ancestor(idKey).list();

    	for (Invite invite: invites) {
    		User inviter = ofy().load().type(User.class).id(invite.getInviter()).now();
    		InviteData inviteEntity = new InviteData(invite, inviter);
    		response.add(inviteEntity);
    	}
    	return response;
    }
    
    @ApiMethod(name = "getAccepts")
    public List<AcceptData> getAccepts(@Named("id") Long id) {
    	
    	List<AcceptData> response = new LinkedList<AcceptData>();
    	Key<User> idKey = Key.create(User.class, id);

    	List<AcceptInvite> accepts = ofy().load().type(AcceptInvite.class).ancestor(idKey).list();

    	for (AcceptInvite accept: accepts) {
    		AcceptData inviteEntity = new AcceptData(accept);
    		response.add(inviteEntity);
    	}
    	return response;
    }    
    
    @ApiMethod(name = "inviteReceived")
    public void inviteReceived(@Named("id") Long id, @Named("inviteeId") Long inviteeId) {
    	Key<User> userKey = Key.create(User.class, inviteeId);
    	Key<Invite> inviteKey = Key.create(userKey, Invite.class, id);
    	ofy().delete().key(inviteKey).now();
    }   

    @ApiMethod(name = "acceptReceived")
    public void acceptReceived(@Named("id") Long id, @Named("inviterId") Long inviterId) {
    	Key<User> userKey = Key.create(User.class, inviterId);
    	Key<AcceptInvite> acceptKey = Key.create(userKey, AcceptInvite.class, id);
    	ofy().delete().key(acceptKey).now();
    }   
}
