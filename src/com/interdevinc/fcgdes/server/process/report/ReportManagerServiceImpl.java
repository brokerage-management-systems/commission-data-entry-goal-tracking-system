package com.interdevinc.fcgdes.server.process.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;
import com.interdevinc.fcgdes.client.process.report.ReportManagerService;
import com.interdevinc.fcgdes.server.process.database.DatabaseConnect;

public class ReportManagerServiceImpl extends RemoteServiceServlet implements ReportManagerService
{
	private static final long serialVersionUID = -5405689733264317552L;
	private DatabaseConnect dbc;
	private Broker brokerWithGoalData;
	private Goal goalData;
	private Gross grossData;

	private String commissionBegin;
	private String commissionDate;
	private String commissionEnd;

	private ArrayList<Gross> grossValues;

	public ReportManagerServiceImpl()
	{

	}

	/**
	 * METHOD: GENERATE GROSS REPORT
	 * 
	 * @param g - gross object containing Report Dates and Rep Number
	 * @return - returns an array list of gross objects to be formated for the Gross Report (GROSS)
	 */
	public ArrayList<Gross> generateGrossReportData(Gross g)
	{
		commissionBegin = g.getCommissionBeginDate();
		commissionDate  = g.getCommissionDayDate();
		commissionEnd   = g.getCommissionEndDate();

		if (g.getRepNumber().equals("ALL")) {
			ArrayList<String[]> brokerArrayList = new ArrayList<String[]>();
			brokerArrayList = retrieveBrokerList();
			createGrossOjbects(brokerArrayList);
		} else {
			ArrayList<String[]> brokerArrayList = new ArrayList<String[]>();
			brokerArrayList = retrieveSingleBrokerInfo(g.getRepNumber());
			createGrossOjbects(brokerArrayList);		
		}

		return grossValues;
	}

	/**
	 * METHOD: GENERATE GOAL REPORT
	 * @param g - goal object containing Report Date and Rep Number
	 * @return - returns a GoalChartData object to be formated for the Goal Report (GOAL)
	 */
	public Broker generateGoalReportData(Goal g)
	{
		brokerWithGoalData = new Broker();
		goalData = new Goal();
		grossData = new Gross();

		// Get the Brokers goals by month for the current year.
		ArrayList<String[]> currentYearDates = createArrayOfMonthsToCurrentMonth(g.getGoalYear());			
		addCurrentGoalsToGoalChartData(g, currentYearDates);

		// Get the Brokers gross by month for the current year.
		addMonthlyGrossTotalsToGoal(g, currentYearDates, "currentYear");

		// Get the Brokers gross by month for the previous year.
		int yearAsInt = Integer.parseInt(g.getGoalYear());
		--yearAsInt;
		ArrayList<String[]> previousYearDates = createArrayOfMonthsToCurrentMonth(Integer.toString(yearAsInt));
		addMonthlyGrossTotalsToGoal(g, previousYearDates, "previousYear");

		String[] repInfo = retrieveSingleBrokerInfoGoal(g.getRepNumber());

		brokerWithGoalData.setFirstname(repInfo[0]);
		brokerWithGoalData.setLastname(repInfo[1]);
		brokerWithGoalData.setGoal(goalData);
		brokerWithGoalData.setGross(grossData);
			
		return brokerWithGoalData;
	}

	/**
	 * METHOD: ADD CURRENT GOALS TO GOAL CHART DATA
	 * @param g - uses goal for repNumber and goalYear (GOAL)
	 */
	private void addCurrentGoalsToGoalChartData(Goal g, ArrayList<String[]> months)
	{
		// Get the Brokers goals for the requested year.
		String goalsQuery = new String("SELECT totalGross, moneyRaised, newAccounts "+
				"FROM GoalData WHERE repNumber='"+g.getRepNumber()+"' AND goalYear='"+g.getGoalYear()+"'");
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = statement.executeQuery(goalsQuery);
			String[] grossGoal = new String[months.size()];
			String[] moneyRaisedGoal = new String[months.size()];
			String[] newAcctGoal = new String[months.size()];
			if (results.next()) {
				String grossVal = new StringBuilder(Double.toString(results.getDouble(1) / 12)).toString();
				String moneyVal = new StringBuilder(Double.toString(results.getDouble(2) / 12)).toString();
				String acctVal = new StringBuilder(Double.toString(results.getDouble(3) / 12)).toString();
				for (int month = 0; month < months.size(); ++month) {
					grossGoal[month] = grossVal;
					moneyRaisedGoal[month] = moneyVal;
					newAcctGoal[month] = acctVal;
				}
			} else {
				for (int month = 0; month < months.size(); ++month) {
					grossGoal[month] = "0.00";
					moneyRaisedGoal[month] = "0.00";
					newAcctGoal[month] = "0.00";
				} 
			}
			goalData.setGoalTotalGross(grossGoal);
			goalData.setGoalTotalMoneyRaised(moneyRaisedGoal);
			goalData.setGoalTotalNewAccounts(newAcctGoal);
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * METHOD: ADD MONTHLY GROSS TOTALS TO GOAL CHART DATA
	 * 
	 * @param goal, months, type - uses goal for repNumber, months for start
	 *  and end dates for each month, & type for current/previous year (GOAL)
	 */
	private void addMonthlyGrossTotalsToGoal(Goal goal, ArrayList<String[]> months, String type)
	{
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = null;
			String[] grossGoal = new String[months.size()];
			String[] moneyRaisedGoal = new String[months.size()];
			String[] newAcctGoal = new String[months.size()];
			for (int a = 0; a < months.size(); ++a) {
				// Totals for Gross(nfsGross + legentGross + insuranceGross), Money Raised, & New Accounts(newAccounts - renegs)
				String monthTotalGrossQuery = new String("SELECT SUM(nfsGross), SUM(legentGross), SUM(insuranceGross), SUM(moneyRaised), SUM(newAccounts), SUM(renegs) FROM GrossData WHERE repNumber='"+goal.getRepNumber()+"' AND grossDate BETWEEN '"+months.get(a)[0]+"' AND '"+months.get(a)[1]+"'");
				results = statement.executeQuery(monthTotalGrossQuery);
				if (results.next()) {
					//TODO Deal with nulls...
					grossGoal[a] = Double.toString(results.getDouble(1) + results.getDouble(2) + results.getDouble(3));
					moneyRaisedGoal[a] = Double.toString(results.getDouble(4));
					newAcctGoal[a] = Integer.toString(results.getInt(5) - results.getInt(6));
				} else {
					grossGoal[a] = ("0.00");
					moneyRaisedGoal[a] = ("0.00");
					newAcctGoal[a] = ("0.00");
				}
			}
			if (type.equals("currentYear")) {
				grossData.setCurrentYearTotalGross(grossGoal);
				grossData.setCurrentYearTotalMoneyRaised(moneyRaisedGoal);
				grossData.setCurrentTotalNewAccounts(newAcctGoal);
			} else if (type.equals("previousYear")) {
				grossData.setPreviousYearTotalGross(grossGoal);
				grossData.setPreviousYearTotalMoneyRaised(moneyRaisedGoal);
				grossData.setPreviousTotalNewAccounts(newAcctGoal);
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * METHOD: CREATE ARRAY OF MONTHS
	 * Database needs to store months consecutively, or data will be wrong.
	 *  
	 * @param year - uses year to create an array of the start and end dates for each month
	 * @return - returns start and end dates for each month (GOAL)
	 */
	private ArrayList<String[]> createArrayOfMonthsToCurrentMonth(String year)
	{
		Calendar cal = Calendar.getInstance(); 
		int numOfMonths = cal.get(Calendar.MONTH) + 1;
		ArrayList<String[]> dateArrayList = new ArrayList<String[]>();
		String datesQuery = "SELECT commissionMonthStart, commissionMonthEnd "+
			"FROM `commissionMonths` WHERE financialYear='"+year+"'";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = statement.executeQuery(datesQuery);
			if (results != null) {
				for (int a = 0; a <= numOfMonths; ++a) {
					results.next();
					String[] dateArray = new String[2];
					dateArray[0] = results.getString(1);
					dateArray[1] = results.getString(2);
					dateArrayList.add(dateArray);
				}
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return dateArrayList;
	}

	/**
	 * METHOD: CREATE GROSS OBJECTS
	 * 
	 * @param brokerArrayList - array list containing Brokers included in the report.
	 * @return - returns an array list of gross objects to be formated for the Gross Report (GROSS)
	 */
	private void createGrossOjbects(ArrayList<String[]> brokerArrayList)
	{
		grossValues = new ArrayList<Gross>();
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = null;
			ResultSet results = null;
			ResultSet totalResults = null;
			for (int a = 0; a < brokerArrayList.size(); ++a) {
				Gross grossObject = new Gross();
				grossObject.setRepName(brokerArrayList.get(a)[1]);
				grossObject.setRepNumber(brokerArrayList.get(a)[0]);
				String grossQuery = "SELECT nfsGross, legentGross, dealGross, insuranceGross, moneyRaised, newAccounts, renegs FROM GrossData WHERE repNumber='"+brokerArrayList.get(a)[0]+"' AND grossDate='"+commissionDate+"'";
				//System.out.println(grossQuery);
				String grossTotalQuery = "SELECT SUM(nfsGross), SUM(legentGross), SUM(dealGross), SUM(insuranceGross), SUM(moneyRaised), SUM(newAccounts), SUM(renegs) FROM GrossData WHERE repNumber='"+brokerArrayList.get(a)[0]+"' AND grossDate BETWEEN '"+commissionBegin+"' AND '"+commissionEnd+"'";
				//System.out.println(grossTotalQuery);
				statement = dbc.getConnection().createStatement();
				results = statement.executeQuery(grossQuery);
				if (results.next()) {
					grossObject.setNFSGross((results.getString(1)!=null)?results.getString(1):"0.00");
					grossObject.setLegentGross((results.getString(2)!=null)?results.getString(2):"0.00");
					grossObject.setDealGross((results.getString(3)!=null)?results.getString(3):"0.00");
					grossObject.setInsuranceGross((results.getString(4)!=null)?results.getString(4):"0.00");
					grossObject.setMoneyRaised((results.getString(5)!=null)?results.getString(5):"0.00");
					grossObject.setNewAccount((results.getString(6)!=null)?results.getString(6):"0");
					grossObject.setReneg((results.getString(7)!=null)?results.getString(7):"0");
				} else {
					grossObject.setNFSGross("0.00");
					grossObject.setLegentGross("0.00");
					grossObject.setDealGross("0.00");
					grossObject.setInsuranceGross("0.00");
					grossObject.setMoneyRaised("0.00");
					grossObject.setNewAccount("0");
					grossObject.setReneg("0");
				}
				totalResults = statement.executeQuery(grossTotalQuery);
				if (totalResults.next()) {
					grossObject.setTotalNfsGross((totalResults.getString(1)!=null)?totalResults.getString(1):"0.00");
					grossObject.setTotalLegentGross((totalResults.getString(2)!=null)?totalResults.getString(2):"0.00");
					grossObject.setTotalDealGross((totalResults.getString(3)!=null)?totalResults.getString(3):"0.00");
					grossObject.setTotalInsuranceGross((totalResults.getString(4)!=null)?totalResults.getString(4):"0.00");
					grossObject.setTotalMoneyRaisedGross((totalResults.getString(5)!=null)?totalResults.getString(5):"0.00");
					grossObject.setTotalNewAcct((totalResults.getString(6)!=null)?totalResults.getString(6):"0");
					grossObject.setTotalReneg((totalResults.getString(7)!=null)?totalResults.getString(7):"0");
				} else {
					grossObject.setTotalNfsGross("0.00");
					grossObject.setTotalLegentGross("0.00");
					grossObject.setTotalDealGross("0.00");
					grossObject.setTotalInsuranceGross("0.00");
					grossObject.setTotalMoneyRaisedGross("0.00");
					grossObject.setTotalNewAcct("0");
					grossObject.setTotalReneg("0");
				}
				//System.out.println(grossObject.getRepName()+" - "+grossObject.getRepNumber());
				grossValues.add(grossObject);
			}
			statement.close();
			results.close();
			totalResults.close();
			dbc.closeConnection();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	/**
	 * METHOD: RETRIEVE BROKER LIST
	 * 
	 * @return - returns an array list of ACTIVE Brokers Information, repNumber, fullName (GROSS)
	 */
	private ArrayList<String[]> retrieveBrokerList()
	{
		ArrayList<String[]> repInfoList = new ArrayList<String[]>();
		String datesQuery = "SELECT repNumber, firstName, lastName FROM Brokers WHERE status='1' ORDER BY lastName ASC";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = statement.executeQuery(datesQuery);
			if (results != null) {
				while (results.next()) {
					String[] repInfo = new String[2];
					repInfo[0] = results.getString(1);
					repInfo[1] = results.getString(2)+" "+results.getString(3);
					repInfoList.add(repInfo);
				}
			} else {
				String[] repInfo = new String[2];
				repInfo[0] = "Unknown";
				repInfo[1] = "Broker";
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return repInfoList;
	}

	/**
	 * METHOD: RETRIEVE SINGLE BROKER INFO
	 * 
	 * @param repNumber - rep number of single Broker included in the report.
	 * @return - returns an array list of Broker Information, repNumber, fullName (GROSS)
	 */
	private ArrayList<String[]> retrieveSingleBrokerInfo(String repNumber)
	{
		ArrayList<String[]> brokerArrayList = new ArrayList<String[]>();
		String datesQuery = "SELECT repNumber, firstName, lastName FROM Brokers WHERE repNumber='"+repNumber+"'";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = statement.executeQuery(datesQuery);
			String[] repInfo = new String[2];
			if (results != null) {
				while (results.next()) {
					repInfo[0] = results.getString(1);
					repInfo[1] = results.getString(2)+" "+results.getString(3);
					brokerArrayList.add(repInfo);
				}
			} else {
				repInfo[0] = "Unknown";
				repInfo[1] = "Broker";
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return brokerArrayList;
	}

	/**
	 * METHOD: RETRIEVE SINGLE BROKER INFO
	 * 
	 * @param repNumber - rep number of single Broker included in the report.
	 * @return - returns an array of Broker Information, firstName, lastName (Goal)
	 */
	private String[] retrieveSingleBrokerInfoGoal(String repNumber)
	{
		String[] repInfo = new String[2];
		String datesQuery = "SELECT firstName, lastName FROM Brokers WHERE repNumber='"+repNumber+"'";
		try {
			dbc = new DatabaseConnect("local", "des");
			Statement statement = dbc.getConnection().createStatement();
			ResultSet results = statement.executeQuery(datesQuery);
			if (results != null) {
				while (results.next()) {
					repInfo[0] = results.getString(1);
					repInfo[1] = results.getString(2);
				}
			} else {
				repInfo[0] = "Unknown";
				repInfo[1] = "Broker";
			}
			results.close();
			statement.close();
			dbc.closeConnection();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return repInfo;
	}
}