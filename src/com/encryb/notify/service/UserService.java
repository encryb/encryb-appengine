package com.encryb.notify.service;

import static com.encryb.notify.OfyService.ofy;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import com.encryb.notify.model.AcceptInvite;
import com.encryb.notify.model.Invite;
import com.encryb.notify.model.AcceptData;
import com.encryb.notify.model.InviteData;
import com.encryb.notify.model.Login;
import com.encryb.notify.model.RegistrationData;
import com.encryb.notify.model.User;
import com.encryb.notify.utils.Encryption;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Nullable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;

@Api(name = "encrybuser", version = "v1")
public class UserService {
	
	final int MIN_USER = 10000;
	final int MAX_USER = 500000;
	
	@ApiMethod(name = "createProfile")
    public RegistrationData createProfile(
    		   @Named("userId") Long userId,
    		   @Named("name") String name,
			   @Named("intro") String intro,
			   @Named("pictureUrl") String pictureUrl,
			   @Named("publicKey") String publicKey) {
		
		RegistrationData reg = createLogin(userId);

    	User user = new User(reg.getUserId(), name, intro, pictureUrl, publicKey);
    	ofy().save().entity(user);
		return reg;
    }
	
	private RegistrationData createLogin(Long requestedId) {
		final SecureRandom random = new SecureRandom();
		String password = new BigInteger(130, new SecureRandom()).toString(32);
	    final byte[] passwordHash = Encryption.encryptSHA512(password);
		
	    for (int i = 0; i < 3; i++) {
    		final long userId = (requestedId != null) ? requestedId:
    			random.nextInt((MAX_USER - MIN_USER) + 1) + MIN_USER;
    	
    		Login login = ofy().transact(new Work<Login>() {
    			public Login run() {
	        		Login exitingLogin = ofy().load().type(Login.class).id(userId).now();
	        		if (exitingLogin != null) {
	        			return null;
	        		}
	        		Login newLogin = new Login(userId, passwordHash);
		            
		            ofy().save().entity(newLogin);
		            return newLogin;
	        	}
    		});
    		if (login != null) {
    			RegistrationData reg = new RegistrationData(userId, password);
    			return reg;
    		}
	    }
	    throw new RuntimeException("Could not allocate Id");
	}
	
    @ApiMethod(name = "getProfile")
    public User getProfile(@Named("id") Long id) {
    	User user = ofy().load().type(User.class).id(id).now();
    	return user;
    }
	
    @ApiMethod(name = "setProfile", httpMethod = HttpMethod.PUT)
    public void setProfile(@Named("id") Long id,
    					   @Named("password") @Nullable String password,
    					   @Named("name") String name,
    					   @Named("intro") String intro,
    					   @Named("pictureUrl") String pictureUrl,
    					   @Named("publicKey") String publicKey) {

    	verifyPassword(id, password);
    	User user = new User(id, name, intro, pictureUrl, publicKey);
    	ofy().save().entity(user);
    }
    
    private void verifyPassword(long id, String password) {
    	// Legacy
    	if (password == null) {
    		return;
    	}
    	Login login = ofy().load().type(Login.class).id(id).now();
    	if (login == null) {
    		throw new RuntimeException("Incorrect login!");
    	}
    	
    	byte[] hash = Encryption.encryptSHA512(password);
    	if (!Arrays.equals(hash, login.getPasswordHash())) {
    		throw new RuntimeException("Incorrect login!");
    	}
    }
    
    @ApiMethod(name = "invite")
    public void invite(@Named("id") Long id,
    				   @Named("password") @Nullable String password,
    				   @Named("inviteeId") Long inviteeId,
    				   @Named("datastoreId") String datastoreId) {
    	Key<User> inviteeKey = Key.create(User.class, inviteeId);
    	
    	verifyPassword(id, password);
    	
    	Invite invite = new Invite(inviteeKey, id, 
    			System.currentTimeMillis() / 1000,
    			datastoreId);
    	
    	ofy().save().entity(invite);
    }
    
    @ApiMethod(name = "acceptInvite")
    public void acceptInvite(@Named("id") Long id,
    						 @Named("password") @Nullable String password,
    						 @Named("inviterId") Long inviter,
    						 @Named("datastoreId") String datastoreId) {
    	
    	verifyPassword(id, password);
    	Key<User> inviterKey = Key.create(User.class, inviter);
    	AcceptInvite accept = new AcceptInvite(inviterKey, id, 
    			System.currentTimeMillis() / 1000, 
    			datastoreId);
    	ofy().save().entity(accept);
    }
    
    @ApiMethod(name = "getInvites")
    public List<InviteData> getInvites(@Named("id") Long id,
    								   @Named("password") @Nullable String password) {
    	
    	List<InviteData> response = new LinkedList<InviteData>();
    	Key<User> idKey = Key.create(User.class, id);

    	List<Invite> invites = ofy().load().type(Invite.class).ancestor(idKey).list();

    	for (Invite invite: invites) {
    		User inviter = ofy().load().type(User.class).id(invite.getInviter()).now();
    		InviteData inviteEntity = new InviteData(invite, inviter);
    		response.add(inviteEntity);
    	}
    	
    	if (!response.isEmpty()) {
    		verifyPassword(id, password);
    	}
    	
    	return response;
    }
    
    @ApiMethod(name = "getAccepts")
    public List<AcceptData> getAccepts(@Named("id") Long id, 
    								   @Named("password") @Nullable String password) {
    	
    	List<AcceptData> response = new LinkedList<AcceptData>();
    	Key<User> idKey = Key.create(User.class, id);

    	List<AcceptInvite> accepts = ofy().load().type(AcceptInvite.class).ancestor(idKey).list();

    	for (AcceptInvite accept: accepts) {
    		AcceptData inviteEntity = new AcceptData(accept);
    		response.add(inviteEntity);
    	}
    	

    	if (!response.isEmpty()) {
    		verifyPassword(id, password);
    	}
    	return response;
    }    
    
    @ApiMethod(name = "inviteReceived")
    public void inviteReceived(@Named("id") Long id, 
    						   @Named("password") @Nullable String password, 
    						   @Named("inviteeId") Long inviteeId) {
    	
    	verifyPassword(id, password);
    	
    	Key<User> userKey = Key.create(User.class, inviteeId);
    	Key<Invite> inviteKey = Key.create(userKey, Invite.class, id);
    	ofy().delete().key(inviteKey).now();
    }   

    @ApiMethod(name = "acceptReceived")
    public void acceptReceived(@Named("id") Long id, 
    						   @Named("password") @Nullable String password,
    		@Named("inviterId") Long inviterId) {
    	
    	verifyPassword(id, password);
    	
    	Key<User> userKey = Key.create(User.class, inviterId);
    	Key<AcceptInvite> acceptKey = Key.create(userKey, AcceptInvite.class, id);
    	ofy().delete().key(acceptKey).now();
    }   
}
