package com.interdevinc.fcgdes.server.process.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.AuthenticatedUser;
import com.interdevinc.fcgdes.client.process.user.AuthenticateUser;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class AuthenticateUserImpl extends RemoteServiceServlet implements AuthenticateUser
{
	private static final long serialVersionUID = -64485569086721600L;
	private DatabaseConnect dbc;
	private Statement statement;
	private ResultSet results;
	private final Date date = new Date();
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

	public AuthenticateUserImpl()
	{

	}
	
	/**
	 * METHOD:	AUTHENTICATE USER
	 * 
	 * @param u - username
	 * @param p - password
	 * @return authenticatedUser
	 */
	public AuthenticatedUser authenticateUser(String u, String p)
	{
		AuthenticatedUser authenticatedUser = null;
		final String userQuery="SELECT userid, username, emailAddress "+
			"FROM users WHERE (username='"+u+"' AND password='"+p+"')";
		try {
			dbc = new DatabaseConnect("local", "auth");
			statement  = dbc.getConnection().createStatement();
			statement.execute(userQuery);
			results = statement.getResultSet();
			if (results != null) {
				if (results.next()) {
					authenticatedUser = new AuthenticatedUser(results.getString(1), results.getString(2), results.getString(3));
					System.out.println("User: " + results.getString(1) + " - " + results.getString(2) + " successfully logged in @" + sdf.format(date));
				} else {
					System.out.println("Invalid login for user: " + u + " - @" + sdf.format(date));
				}
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return authenticatedUser;
	}
}