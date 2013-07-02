package com.interdevinc.fcgdes.server.process.goal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.process.goal.GoalManagerService;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class GoalManagerServiceImpl extends RemoteServiceServlet implements GoalManagerService
{
	private static final long serialVersionUID = 264207790900556592L;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet results;

	private DatabaseConnect dbc;
	
	public GoalManagerServiceImpl()
	{

	}

	/**
	 * METHOD: INSERT GOAL
	 * 
	 * @param goal
	 */
	public boolean insertGoal(Goal goal)
	{
		boolean isUpdated = false;
		final String selectQuery = "SELECT * " +
			"FROM GoalData " + 
			"WHERE repNumber='"+goal.getRepNumber()+"' AND goalYear='"+goal.getGoalYear()+"'";
		final String insertQuery="INSERT INTO GoalData (repNumber,goalYear,totalGross,moneyRaised,newAccounts) VALUES (?,?,?,?,?)";
		try {
			dbc = new DatabaseConnect("local", "des");
			preparedStatement=dbc.getConnection().prepareStatement(insertQuery);
			statement = dbc.getConnection().createStatement();
			results = statement.executeQuery(selectQuery);
			if (!results.next()) {
				preparedStatement.setString(1,goal.getRepNumber());
				preparedStatement.setString(2,goal.getGoalYear());
				preparedStatement.setString(3,goal.getTotalGross());
				preparedStatement.setString(4,goal.getMoneyRaised());
				preparedStatement.setString(5,goal.getNewAccounts());
				preparedStatement.executeUpdate();
				isUpdated = true;
			} else {
				isUpdated = false;
			}
			results.close();
			statement.close();
			preparedStatement.close();
			dbc.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUpdated;
	}

	/**
	 * METHOD: RETRIEVE BROKER GOAL
	 * 
	 * @param repNumber
	 * @param goalYear
	 * @return repGoal
	 */
	public Goal retrieveBrokerGoal(String repNumber, String goalYear)
	{
		Goal repGoal=null;
		final String query="SELECT totalGross, moneyRaised, newAccounts, repNumber " +
			"FROM  GoalData " +
			"WHERE repNumber='"+repNumber+"' AND goalYear='"+goalYear+"'";
		try {
			dbc = new DatabaseConnect("local", "des");
			statement=dbc.getConnection().createStatement();
			results=statement.executeQuery(query);
			if(results.next()){
				repGoal=new Goal();
				repGoal.setTotalGross(results.getString(1));
				repGoal.setMoneyRaised(results.getString(2));
				repGoal.setNewAccounts(results.getString(3));
				repGoal.setRepNumber(repNumber);
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return repGoal;
	}

	/**
	 * METHOD: INSERT GOAL
	 */
	public boolean updateGoal(Goal goal)
	{
		final String insertQuery="UPDATE GoalData " +
			"SET totalGross=?, moneyRaised=?, newAccounts=? " +
			"WHERE (goalYear='"+goal.getGoalYear()+"' AND repNumber='"+goal.getRepNumber()+"')";
		try {
			dbc = new DatabaseConnect("local", "des");
			preparedStatement=dbc.getConnection().prepareStatement(insertQuery);
			preparedStatement.setString(1,goal.getTotalGross());
			preparedStatement.setString(2,goal.getMoneyRaised());
			preparedStatement.setString(3,goal.getNewAccounts());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			dbc.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return true;
	}
}