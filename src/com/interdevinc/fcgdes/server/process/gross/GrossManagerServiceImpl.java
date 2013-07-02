package com.interdevinc.fcgdes.server.process.gross;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Gross;
import com.interdevinc.fcgdes.client.process.gross.GrossManagerService;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class GrossManagerServiceImpl extends RemoteServiceServlet implements GrossManagerService
{
	private static final long serialVersionUID = -5185021824487346609L;
	private DatabaseConnect dbc;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private ResultSet results;

	public GrossManagerServiceImpl()
	{

	}

	/**
	 * METHOD: INSERT GROSS DATA
	 * 
	 * @param broker
	 * @return querySuccessful
	 */
	public boolean insertGrossData(Broker broker)
	{
		boolean querySuccessful = false;
		final String insertQuery="INSERT INTO "+
			"GrossData(repNumber,grossDate,dealGross,insuranceGross,legentGross,moneyRaised,nfsGross,newAccounts,renegs)"+
			"VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			dbc = new DatabaseConnect("local", "des");
			preparedStatement=dbc.getConnection().prepareStatement(insertQuery);
			Gross gross=broker.getGross();
			preparedStatement.setString(1,broker.getRepNumber());
			preparedStatement.setString(2,gross.getGrossDate());
			preparedStatement.setString(3,gross.getDealGross());
			preparedStatement.setString(4,gross.getInsuranceGross());
			preparedStatement.setString(5,gross.getLegentGross());
			preparedStatement.setString(6,gross.getMoneyRaised());
			preparedStatement.setString(7,gross.getNFSGross());
			preparedStatement.setString(8,gross.getNewAccount());
			preparedStatement.setString(9,gross.getReneg());
			if (preparedStatement.execute()) {
				querySuccessful = true;
			}
			preparedStatement.close();
			dbc.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return querySuccessful;
	}

	/**
	 * METHOD: RETRIEVE REP GROSS BY DATE RANGE
	 * 
	 * @param repNumber
	 * @param startDate
	 * @param endDate
	 * @return grossList
	 */
	public ArrayList<Gross> retrieveGrossDataRange(String repNumber, String startDate, String endDate)
	{
		final ArrayList<Gross> grossList=new ArrayList<Gross>();
		final String grossQuery="SELECT grossID, repNumber, grossDate, dealGross, insuranceGross, legentGross, moneyRaised, newAccounts, nfsGross, renegs " +
			"FROM GrossData " +
			"WHERE repNumber='"+repNumber+"' AND grossDate " +
			"BETWEEN '"+startDate+"' AND '"+endDate+"' " +
			"ORDER BY grossDate ASC";
		try {
			dbc = new DatabaseConnect("local", "des");
			statement=dbc.getConnection().createStatement();
			results=statement.executeQuery(grossQuery);
			if (results!=null) {
				while (results.next()) {
					grossList.add(new Gross(results.getString(1),results.getString(2),results.getString(3),
						results.getString(4),results.getString(5),results.getString(6),results.getString(7),
						results.getString(8),results.getString(9),results.getString(10)));
				}
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return grossList;
	}

	/**
	 * METHOD: UPDATE GROSS DATA
	 * 
	 * @param gross
	 * @return querySuccessful
	 */
	public boolean updateGrossData(Gross gross)
	{
		boolean querySuccessful = false;
		final String updateQuery="UPDATE GrossData " +
			"SET "+
			"dealGross='"+gross.getDealGross()+"', " +
			"insuranceGross='"+gross.getInsuranceGross()+"', " +
			"legentGross='"+gross.getLegentGross()+"', " +
			"moneyRaised='"+gross.getMoneyRaised()+"', " +
			"nfsGross='"+gross.getNFSGross()+"', " +
			"newAccounts='"+gross.getNewAccount()+"', " +
			"renegs='"+gross.getReneg()+"' " +
			"WHERE grossID='"+gross.getGrossID()+"'";
		//System.out.println(updateQuery); // For debugging
		try {
			dbc = new DatabaseConnect("local", "des");
			statement=dbc.getConnection().createStatement();
			int numOfExecutions = statement.executeUpdate(updateQuery);
			if (numOfExecutions > 0) {
				querySuccessful = true;
			}
			statement.close();
			dbc.closeConnection();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return querySuccessful;
	}
}