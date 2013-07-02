package com.interdevinc.fcgdes.client.process.user;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.AuthenticatedUser;


public interface AuthenticateUserAsync {

    void authenticateUser(String u, String p, AsyncCallback<AuthenticatedUser> callback);

}
