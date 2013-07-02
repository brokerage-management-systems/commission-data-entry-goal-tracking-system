package com.interdevinc.fcgdes.server.process.date;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.Date;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class DateManagerServiceImpl extends RemoteServiceServlet
{
	private static final long serialVersionUID = -6821055412179605699L;
	private DatabaseConnect dbc;
	
	public DateManagerServiceImpl()
	{

	}
	
	public Date retrieveCurrentMonthDates(String date)
	{
		Date currentMonthDates = null;
		int intDate = Integer.parseInt(date);
		String grossQuery="";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement=dbc.getConnection().createStatement();
			ResultSet results=statement.executeQuery(grossQuery);
			if (results!=null) {
				while (results.next()) {
					int startDate = results.getInt(1);
					int endDate = results.getInt(2);
					if (intDate > startDate && intDate < endDate) {
						currentMonthDates = new Date();
						currentMonthDates.setCommissionMonthStart(results.getString(1));
						currentMonthDates.setCommissionMonthEnd(results.getString(2));
						currentMonthDates.setPreviousMonth(results.getString(3));
						currentMonthDates.setNextMonth(results.getString(4));
						currentMonthDates.setMonthLabel(results.getString(5));
					}
				}
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return currentMonthDates;
	}
}
