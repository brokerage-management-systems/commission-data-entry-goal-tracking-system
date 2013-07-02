package com.interdevinc.fcgdes.server.process.broker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.process.broker.BrokerManagerService;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class BrokerManagerServiceImpl extends RemoteServiceServlet implements BrokerManagerService
{
	private static final long serialVersionUID = 5474890457794058535L;
	private DatabaseConnect dbc;
	private final String brokerTable="Brokers";

	public BrokerManagerServiceImpl()
	{

	}
	
	/**
	 * METHOD: INSERT BROKER
	 * Inserts a broker into the database
	 * 
	 * @param broker
	 * @return insertSuccess
	 */
	public boolean insertBroker(Broker broker)
	{
		boolean insertSuccess = false;
		final String query="INSERT INTO "+brokerTable+"(brokerID,repNumber,firstName,lastName,status) VALUES (?,?,?,?,?)";
		try {
			dbc = new DatabaseConnect("local", "des");
			PreparedStatement statement=dbc.getConnection().prepareStatement(query);
			statement.setInt(1,0);
			statement.setString(2,broker.getRepNumber());
			statement.setString(3,broker.getFirstname());
			statement.setString(4,broker.getLastname());
			statement.setInt(5,(broker.getStatus()?1:0));
			int result = statement.executeUpdate();
			statement.close();
			dbc.closeConnection();
			if (result > 0) { 
				insertSuccess = true;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return insertSuccess;
		}
		return insertSuccess;
	}

	/**
	 * METHOD:	RETRIEVE BROKER LIST
	 * Retrieves and arraylist of broker objects
	 * 
	 * @return brokerList
	 */
	public ArrayList<Broker> retrieveBrokerList()
	{
		ArrayList<Broker> brokerList=new ArrayList<Broker>();
		final String query="SELECT * FROM  "+brokerTable+" ORDER BY lastName ASC";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement=dbc.getConnection().createStatement();
			ResultSet results=statement.executeQuery(query);
			if (results!=null) {
				while (results.next()) {
					brokerList.add(new Broker(results.getInt(1),
							results.getString(2),results.getString(3),
							results.getString(4),results.getBoolean(5))
					);	
				}
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return (brokerList.size()!=0)?brokerList:null;
	}

	/**
	 * METHOD:	INSERT BROKER
	 * Inserts a broker into the database
	 * 
	 * @param broker
	 * @return boolean
	 */
	public boolean updateBroker(Broker broker)
	{
		//System.out.println("Updating "+broker.toString());
		final String query="UPDATE "+brokerTable+
			" SET repNumber=?, firstName=?, lastName=?, status=?" +
			" WHERE brokerID=?";
		try {
			dbc = new DatabaseConnect("local", "des");
			PreparedStatement prepStatement=dbc.getConnection().prepareStatement(query);
			prepStatement.setString(1,broker.getRepNumber());
			prepStatement.setString(2,broker.getFirstname());
			prepStatement.setString(3,broker.getLastname());
			prepStatement.setInt(4,(broker.getStatus()?1:0));
			prepStatement.setInt(5,broker.getID());
			prepStatement.executeUpdate();
			prepStatement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return false;
		}
		return true;
	}
}