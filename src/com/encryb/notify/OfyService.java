package com.encryb.notify;

import com.encryb.notify.model.AcceptInvite;
import com.encryb.notify.model.Invite;
import com.encryb.notify.model.Login;
import com.encryb.notify.model.User;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {
    static {
        factory().register(User.class);
        factory().register(Invite.class);
        factory().register(AcceptInvite.class);
        factory().register(Login.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}