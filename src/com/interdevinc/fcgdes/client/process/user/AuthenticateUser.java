package com.interdevinc.fcgdes.client.process.user;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.interdevinc.fcgdes.client.model.AuthenticatedUser;

@RemoteServiceRelativePath("authenticateUser")
public interface AuthenticateUser extends RemoteService {

    AuthenticatedUser authenticateUser(String u, String p);

}