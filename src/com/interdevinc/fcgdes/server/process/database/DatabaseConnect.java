/**
 * DatabaseConnect
 * Created on Sept 28, 2009 11:33AM
 * Modified on May 20, 2011 21:07PM
 * Modified on June 23, 2011 21:07PM for GWT Project
 * @author Matthew Weppler
 * copyright 2011 InterDev Inc.
 */
package com.interdevinc.fcgdes.server.process.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseConnect
{
    private Connection connection;
    private HashMap<String, String> databaseSettings = new HashMap<String, String>();

    public DatabaseConnect(String connectionType, String database)
    {
    	if (database.equals("auth")) {
    		databaseSettings.put("database", "fcg_appauth");
    	} else {
    		databaseSettings.put("database", "fcg_desapp");
    	}
    	if (connectionType.equals("local")) {
    		databaseSettings.put("host",     "localhost");
    		databaseSettings.put("username", "fcgdesapp");
    		databaseSettings.put("password", "+&Zk1}lp7^oV");
    	} else if (connectionType.equals("livetesting")) {
    		databaseSettings.put("host",     "domain.com");
    		if (database.equals("auth")) {
    			databaseSettings.put("database", "_fcgappauth");
    		} else {
    			databaseSettings.put("database", "_fcgdesapp");
    		}
    		databaseSettings.put("username", "_fcgdesa");
    		databaseSettings.put("password", "+&Zk1}lp7^oV");
    	} else /*live*/ {
    		databaseSettings.put("host",     "localhost");
    		databaseSettings.put("username", "fcgdesapp");
    		databaseSettings.put("password", "+&Zk1}lp7^oV");
    	}

 		setConnection();
    }

    public Connection getConnection()
    {
        return this.connection;
    }

    public void setConnection()
    {
        try {
            String connectString = new String("jdbc:mysql://" + databaseSettings.get("host") + "/" + databaseSettings.get("database"));
            //System.out.println(connectString);
            connection = DriverManager.getConnection(connectString, databaseSettings.get("username"), databaseSettings.get("password"));
        } catch (SQLException sqle) {
        	//sqle.printStackTrace();
        }
    }

    public void closeConnection()
    {
    	try {
			this.connection.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
    }
}
