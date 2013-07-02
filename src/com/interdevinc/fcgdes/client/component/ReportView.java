package com.interdevinc.fcgdes.client.component;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.AreaChart.Options;
import com.interdevinc.fcgdes.client.component.widget.InformationDialog;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;
import com.interdevinc.fcgdes.client.process.report.ReportManagerService;
import com.interdevinc.fcgdes.client.process.report.ReportManagerServiceAsync;

public class ReportView extends Composite{

	private int numOfMonths;

	private VerticalPanel chartVerticalPanel;
	private FlexTable dataTable;

	private ReportManagerServiceAsync reportService;
	private GoalReportHandler goalReportHandler;
	private GrossReportHandler grossReportHandler;

	private InformationDialog dialog = new InformationDialog();

	public ReportView(Gross gross){
		initRemoteProcedureWorkers("grossReport");
		generateGrossReport(gross);
		initWidget(chartVerticalPanel);
	}

	public ReportView(Goal goal){
		initRemoteProcedureWorkers("goalReport");
		generateGoalReport(goal);
		initWidget(chartVerticalPanel);
	}

	public void generateGrossReport(Gross gross){

		// Build Table Header
		buildGrossTableHeader(gross);
		// Execute RPC
		reportService.generateGrossReportData(gross, grossReportHandler);
	}

	public void generateGoalReport(Goal goal){
		dialog.loadReportLoadingDialog();
		dialog.show();

		// Number of Months for Goals.
		Date date = new Date();
		DateTimeFormat dtf = DateTimeFormat.getFormat("M");
		numOfMonths = Integer.parseInt(dtf.format(date)) + 1;

		// Build Chart Header
		buildGoalChartHeader();

		// Execute RPC
		reportService.generateGoalReportData(goal, goalReportHandler);
	}

	/**
	 * METHOD: BUILD GOAL CHART HEADER
	 * @param goal - 	 */
	private void buildGoalChartHeader() {
		//TODO Build the Goal Chart Headers.
		chartVerticalPanel = new VerticalPanel();
	}

	/**
	 * METHOD: BUILD GOAL CHART - (GOALS)
	 * @param brokerWithGoalData	- Takes Broker Object which also contains Goal Data	*/
	private void buildGoalChart(final Broker brokerWithGoalData) {

		HTML header = new HTML("<h1>Goals for: " + brokerWithGoalData.getNameFirstLast() +"</h1>");
		chartVerticalPanel.add(header);

		Runnable onLoadCallback = new Runnable() {
			public void run() {
				AreaChart grossChart = new AreaChart(createTotalGrossTable(brokerWithGoalData), createOptions("Total Gross"));
				chartVerticalPanel.add(grossChart);
				AreaChart moneyChart = new AreaChart(createMoneyRaisedTable(brokerWithGoalData), createOptions("Money Raised"));
				chartVerticalPanel.add(moneyChart);
				AreaChart accountsChart = new AreaChart(createNewAccountsTable(brokerWithGoalData), createOptions("New Accounts"));
				chartVerticalPanel.add(accountsChart);
				dialog.hide();
			}
		};
		VisualizationUtils.loadVisualizationApi(onLoadCallback, AreaChart.PACKAGE);
	}

	/**
	 * METHOD: CREATE OPTIONS - (GOALS)
	 * @param chartTitle - Takes a string as the Charts Title
	 * @return - Returns the options for the Area Table	 */
	private Options createOptions(String chartTitle) {
		Options options = Options.create();
		options.setWidth(450);
		options.setHeight(250);
		options.setTitle("Goal ("+chartTitle+")");
		return options;
	}

	/**
	 * METHOD: CREATE TOTAL GROSS TABLE - (GOALS)
	 * @param goalData - Takes a Brokers Goal Data
	 * @return - Returns an Area Table of Total Gross Goals	 */
	private AbstractDataTable createTotalGrossTable(Broker goalData) {

		String[] monthLabels = {"Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov"};
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Month");
		data.addColumn(ColumnType.NUMBER, "Goal");
		data.addColumn(ColumnType.NUMBER, "Current Year");
		data.addColumn(ColumnType.NUMBER, "Previous Year");
		data.addRows(numOfMonths);

		for (int a = 0; a < data.getNumberOfRows(); ++a) {
			data.setValue(a, 0, monthLabels[a]);
			data.setValue(a, 1, Double.parseDouble(goalData.getGoal().getGoalTotalGross()[a]));
			data.setValue(a, 2, Double.parseDouble(goalData.getGross().getCurrentYearTotalGross()[a]));
			data.setValue(a, 3, Double.parseDouble(goalData.getGross().getPreviousYearTotalGross()[a]));
		}

		return data;
	}

	/**
	 * METHOD: CREATE  MONEY RAISED TABLE - (GOALS)
	 * @param goalData - Takes a Brokers Goal Data
	 * @return - Returns an Area Table of Money Raised Goals	 */
	private AbstractDataTable createMoneyRaisedTable(Broker goalData) {
		String[] monthLabels = {"Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov"};
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Month");
		data.addColumn(ColumnType.NUMBER, "Goal");
		data.addColumn(ColumnType.NUMBER, "Current Year");
		data.addColumn(ColumnType.NUMBER, "Previous Year");
		data.addRows(numOfMonths);
		for (int a = 0; a < data.getNumberOfRows(); ++a) {
			data.setValue(a, 0, monthLabels[a]);
			data.setValue(a, 1, Double.parseDouble(goalData.getGoal().getGoalTotalMoneyRaised()[a]));
			data.setValue(a, 2, Double.parseDouble(goalData.getGross().getCurrentYearTotalMoneyRaised()[a]));
			data.setValue(a, 3, Double.parseDouble(goalData.getGross().getPreviousYearTotalMoneyRaised()[a]));
		}

		return data;
	}

	/**
	 * METHOD: CREATE NEW ACCOUNTS TABLE - (GOALS)
	 * @param goalData - Takes a Brokers Goal Data
	 * @return - Returns an Area Table of New Account Goals	 */
	private AbstractDataTable createNewAccountsTable(Broker goalData) {
		String[] monthLabels = {"Dec", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov"};
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Month");
		data.addColumn(ColumnType.NUMBER, "Goal");
		data.addColumn(ColumnType.NUMBER, "Current Year");
		data.addColumn(ColumnType.NUMBER, "Previous Year");
		data.addRows(numOfMonths);
		for (int a = 0; a < data.getNumberOfRows(); ++a) {
			data.setValue(a, 0, monthLabels[a]);
			data.setValue(a, 1, Double.parseDouble(goalData.getGoal().getGoalTotalNewAccounts()[a]));
			data.setValue(a, 2, Double.parseDouble(goalData.getGross().getCurrentTotalNewAccounts()[a]));
			data.setValue(a, 3, Double.parseDouble(goalData.getGross().getPreviousTotalNewAccounts()[a]));
		}
		//dialog.hide();
		return data;
	}

	/**
	 * METHOD: BUILD GROSS TABLE HEADER - (GROSS)
	 * @param gross - gross object for Date Cell Data	*/
	private void buildGrossTableHeader(Gross gross) {

		dialog.loadReportLoadingDialog();
		dialog.show();

		// Style the table
		chartVerticalPanel = new VerticalPanel();
		dataTable = new FlexTable();
		chartVerticalPanel.add(dataTable);

		dataTable.setCellSpacing(0);
		dataTable.setCellPadding(3);

		dataTable.setText(0, 0, "Broker Progress Report");
		dataTable.setText(1, 0, "Commission Beginning: " + formatAsReadableDate(gross.getCommissionBeginDate()));
		dataTable.setText(1, 1, "Commission Ending: " + formatAsReadableDate(gross.getCommissionEndDate()));
		dataTable.setText(2, 0, "Commission Date: " + formatAsReadableDate(gross.getCommissionDayDate()));

		insertColumnTitleRow(3);

		dataTable.getFlexCellFormatter().setColSpan(0, 0, 17);
		dataTable.getRowFormatter().setStyleName(0, "black-row");
		dataTable.getFlexCellFormatter().setColSpan(1, 0, 7);
		dataTable.getFlexCellFormatter().setColSpan(1, 1, 7);
		dataTable.getFlexCellFormatter().setColSpan(2, 0, 7);

	}

	/**
	 * METHOD: BUILD GROSS TABLE - (GROSS)
	 * @param gross - Array List of gross objects for body of the report	*/
	private void buildGrossTable(ArrayList<Gross> gross) {

		// Table Headers Rows end at Row 3.
		// Begin adding Broker Gross at Row 4.
		int dataRowStart = 4;

		for (int a = 0; a < gross.size(); ++a) {
			if (dataRowStart == (dataRowStart / 46 * 46)) {
				insertColumnTitleRow(dataRowStart);
				++dataRowStart;
			}
			dataTable.setText(dataRowStart, 0, gross.get(a).getRepName());
			dataTable.setText(dataRowStart, 1, gross.get(a).getRepNumber());
			dataTable.setText(dataRowStart, 2, formatAsCurrency(gross.get(a).getNFSGross()));
			dataTable.setText(dataRowStart, 3, formatAsCurrency(gross.get(a).getTotalNfsGross()));
			dataTable.setText(dataRowStart, 4, formatAsCurrency(gross.get(a).getLegentGross()));
			dataTable.setText(dataRowStart, 5, formatAsCurrency(gross.get(a).getTotalLegentGross()));
			dataTable.setText(dataRowStart, 6, formatAsCurrency(gross.get(a).getDealGross()));
			dataTable.setText(dataRowStart, 7, formatAsCurrency(gross.get(a).getTotalDealGross()));
			dataTable.setText(dataRowStart, 8, formatAsCurrency(gross.get(a).getInsuranceGross()));
			dataTable.setText(dataRowStart, 9, formatAsCurrency(gross.get(a).getTotalInsuranceGross()));
			dataTable.setText(dataRowStart, 10, formatAsCurrency(Double.toString((Double.parseDouble(gross.get(a).getTotalNfsGross()) + Double.parseDouble(gross.get(a).getTotalLegentGross()) + Double.parseDouble(gross.get(a).getTotalDealGross()) + Double.parseDouble(gross.get(a).getTotalInsuranceGross())))));
			dataTable.setText(dataRowStart, 11, formatAsCurrency(gross.get(a).getMoneyRaised()));
			dataTable.setText(dataRowStart, 12, formatAsCurrency(gross.get(a).getTotalMoneyRaisedGross()));
			dataTable.setText(dataRowStart, 13, gross.get(a).getNewAccount());
			dataTable.setText(dataRowStart, 14, gross.get(a).getTotalNewAcct());
			dataTable.setText(dataRowStart, 15, gross.get(a).getReneg());
			dataTable.setText(dataRowStart, 16, gross.get(a).getTotalReneg());

			for (int col = 0; col < 2; ++col) {
				dataTable.getFlexCellFormatter().setStyleName(dataRowStart, col, "table-text");
			}

			for (int col = 2; col < 17; ++col) {
				dataTable.getFlexCellFormatter().setStyleName(dataRowStart, col, "number-align-right table-text");
			}

			if (dataRowStart == dataRowStart / 2 * 2) {
				dataTable.getRowFormatter().addStyleName(dataRowStart, "alternate-row table-text");
			}

			if (dataRowStart == 44) {

			}

			++dataRowStart;
		}

		// Display report footer.
		int footerRowStart = dataRowStart + 1;
		String firmTotals[] = new String[16];
		firmTotals = calculateFirmTotals(gross);
		dataTable.setText(footerRowStart, 0, "Monthly Running Firm Totals: ");
		dataTable.setText(footerRowStart, 1, firmTotals[0]);
		dataTable.setText(footerRowStart, 2, firmTotals[1]);
		dataTable.setText(footerRowStart, 3, firmTotals[2]);
		dataTable.setText(footerRowStart, 4, firmTotals[3]);
		dataTable.setText(footerRowStart, 5, firmTotals[4]);
		dataTable.setText(footerRowStart, 6, firmTotals[5]);
		dataTable.setText(footerRowStart, 7, firmTotals[6]);
		dataTable.setText(footerRowStart, 8, firmTotals[7]);
		dataTable.setText(footerRowStart, 9, "");
		dataTable.setText(footerRowStart, 10, firmTotals[8]);
		dataTable.setText(footerRowStart, 11, firmTotals[9]);
		dataTable.setText(footerRowStart, 12, firmTotals[10]);
		dataTable.setText(footerRowStart, 13, firmTotals[11]);
		dataTable.setText(footerRowStart, 14, firmTotals[12]);
		dataTable.setText(footerRowStart, 15, firmTotals[13]);
		dataTable.setText(footerRowStart+1, 0, "Firm total w/ Legent & Insurance: ");
		dataTable.setText(footerRowStart+1, 1, firmTotals[14]);
		dataTable.setText(footerRowStart+1, 2, firmTotals[15]);

		// Table formatting
		dataTable.getFlexCellFormatter().setColSpan(footerRowStart, 0, 2);
		dataTable.getFlexCellFormatter().setStyleName(footerRowStart, 0, "black-row table-text");
		dataTable.getFlexCellFormatter().setColSpan(footerRowStart+1, 0, 2);
		dataTable.getFlexCellFormatter().setStyleName(footerRowStart+1, 0, "black-row  table-text");
		for (int col = 1; col < 16; ++col) {
			dataTable.getFlexCellFormatter().setStyleName(footerRowStart, col, "black-row number-align-right table-text");
			dataTable.getFlexCellFormatter().setStyleName(footerRowStart+1, col, "black-row number-align-right table-text");
		}

		dialog.hide();
	}

	/**
	 * METHOD: CALCULATE FIRM TOTALS
	 * @param gross - Takes the Gross object and calculates the report totals
	 * @return - String array 14 values.	*/
	private String[] calculateFirmTotals(ArrayList<Gross> gross) {

		String[] firmTotals = new String[16];

		double nfsValues = 0;
		double nfsMTDValues = 0;
		double legentValues = 0;
		double legentMTDValues = 0;
		double dealValues = 0;
		double dealMTDValues = 0;
		double insuranceValues = 0;
		double insuranceMTDValues = 0;
		double moneyValues = 0;
		double moneyMTDValues = 0;
		int newAccountValues = 0;
		int newAccountMTDValues = 0;
		int renegValues = 0;
		int renegMTDValues = 0;
		double firmTotalW = 0;
		double firmTotalWMTD = 0;

		for (int a = 0; a < gross.size(); ++a) {
			nfsValues += Double.parseDouble(gross.get(a).getNFSGross());
			nfsMTDValues += Double.parseDouble(gross.get(a).getTotalNfsGross());
			legentValues += Double.parseDouble(gross.get(a).getLegentGross());
			legentMTDValues += Double.parseDouble(gross.get(a).getTotalLegentGross());
			dealValues += Double.parseDouble(gross.get(a).getDealGross());
			dealMTDValues += Double.parseDouble(gross.get(a).getTotalDealGross());
			insuranceValues += Double.parseDouble(gross.get(a).getInsuranceGross());
			insuranceMTDValues += Double.parseDouble(gross.get(a).getTotalInsuranceGross());
			moneyValues += Double.parseDouble(gross.get(a).getMoneyRaised());
			moneyMTDValues += Double.parseDouble(gross.get(a).getTotalMoneyRaisedGross());
			newAccountValues += Integer.parseInt(gross.get(a).getNewAccount());
			newAccountMTDValues += Integer.parseInt(gross.get(a).getTotalNewAcct());
			renegValues += Integer.parseInt(gross.get(a).getReneg());
			renegMTDValues += Integer.parseInt(gross.get(a).getTotalReneg());
		}

		firmTotalW = nfsValues + legentValues + dealValues + insuranceValues;
		firmTotalWMTD = nfsMTDValues + legentMTDValues + dealMTDValues + insuranceMTDValues;

		firmTotals[0] = formatAsCurrency(Double.toString(nfsValues));
		firmTotals[1] = formatAsCurrency(Double.toString(nfsMTDValues));
		firmTotals[2] = formatAsCurrency(Double.toString(legentValues));
		firmTotals[3] = formatAsCurrency(Double.toString(legentMTDValues));
		firmTotals[4] = formatAsCurrency(Double.toString(dealValues));
		firmTotals[5] = formatAsCurrency(Double.toString(dealMTDValues));
		firmTotals[6] = formatAsCurrency(Double.toString(insuranceValues));
		firmTotals[7] = formatAsCurrency(Double.toString(insuranceMTDValues));
		firmTotals[8] = formatAsCurrency(Double.toString(moneyValues));
		firmTotals[9] = formatAsCurrency(Double.toString(moneyMTDValues));
		firmTotals[10] = (Integer.toString(newAccountValues));
		firmTotals[11] = (Integer.toString(newAccountMTDValues));
		firmTotals[12] = (Integer.toString(renegValues));
		firmTotals[13] = (Integer.toString(renegMTDValues));
		firmTotals[14] = formatAsCurrency(Double.toString(firmTotalW));
		firmTotals[15] = formatAsCurrency(Double.toString(firmTotalWMTD));

		return firmTotals;
	}

	/**
	 * METHOD: FORMAT AS CURRENCY - (COMMUNITY)
	 * @param numberString - Takes a string and format it as currency
	 * @return - String value as currency 	 */
	private String formatAsCurrency(String numberString) {
		//
		NumberFormat nf = NumberFormat.getCurrencyFormat();
		return nf.format(Double.parseDouble(numberString));
	}

	
	/**
	 * METHOD FORMAT AS READABLE DATE
	 * @param textDate
	 * @return
	 */
	private String formatAsReadableDate(String textDate) {
		// Database readable format
		final DateTimeFormat dbFormat = DateTimeFormat.getFormat("yyyyMMdd");
		// Human readable format
		final DateTimeFormat strFormat = DateTimeFormat.getFormat("MM/dd/yyyy");
		Date date = dbFormat.parse(textDate);
		String formattedDateString = strFormat.format(date);
		return formattedDateString;
	}


	/**
	 * METHOD: INIT REMOTE PROCEDURE WORKERS	 */
	private void initRemoteProcedureWorkers(String type) {

		// Initialize the Report Service & RPC Handler
		reportService = (ReportManagerServiceAsync) GWT.create(ReportManagerService.class);

		// Test which Report Handler to Initialize.
		if (type.equals("goalReport")) {
			goalReportHandler = new GoalReportHandler();
		} else if (type.equals("grossReport")) {
			grossReportHandler = new GrossReportHandler();
		}
	}


	/**
	 * METHOD: INSERT COLUMN TITLE ROW - (GROSS)
	 * @param rowNum - Takes a row number and inserts a title row	 */
	private void insertColumnTitleRow(int rowNum) {
		dataTable.setText(rowNum, 0, "Name");
		dataTable.setText(rowNum, 1, "REP#");
		dataTable.setText(rowNum, 2, "NFS");
		dataTable.setText(rowNum, 3, "NFS MTD");
		dataTable.setText(rowNum, 4, "LEGENT");
		dataTable.setText(rowNum, 5, "LEGENT MTD");
		dataTable.setText(rowNum, 6, "DEAL");
		dataTable.setText(rowNum, 7, "DEAL MTD");
		dataTable.setText(rowNum, 8, "INSURANCE");
		dataTable.setText(rowNum, 9, "INSURANCE MTD");
		dataTable.setText(rowNum, 10, "MTD TOTAL");
		dataTable.setText(rowNum, 11, "$$RAISED$$");
		dataTable.setText(rowNum, 12, "MTD");
		dataTable.setText(rowNum, 13, "N/A");
		dataTable.setText(rowNum, 14, "MTD");
		dataTable.setText(rowNum, 15, "RE");
		dataTable.setText(rowNum, 16, "MTD");

		dataTable.getRowFormatter().setStyleName(rowNum, "column-title-row black-row");
		for (int col = 0; col < 17; ++col) {
			dataTable.getFlexCellFormatter().addStyleName(rowNum, col, "table-text");
		}

	}


	/**
	 * 	CLASS: GOAL REPORT HANDLER - (GOALS)
	 * 	@author mweppler
	 *	On Success retrieves and Array List of Goal Objects, passes to buildGoalChart() Method	*/
	private class GoalReportHandler implements AsyncCallback<Broker> {

		public void onSuccess(Broker brokerWithGoalData) {
			buildGoalChart(brokerWithGoalData);
		}

		public void onFailure(Throwable ex) {
			dialog.hide();
			Window.alert("There was an RPC error in the Report Manager Service. Please contact your administrator.");
		}
	}	

	/**
	 * 	CLASS: GROSS REPORT HANDLER - (GROSS)
	 * 	@author mweppler
	 *	On Success retrieves and Array List of Gross Objects, passes to buildGrossTable() Method	*/
	private class GrossReportHandler implements AsyncCallback<ArrayList<Gross>> {

		public void onSuccess(ArrayList<Gross> gross) {
			buildGrossTable(gross);
		}

		public void onFailure(Throwable ex) {
			dialog.hide();
			Window.alert("There was an RPC error in the Report Manager Service. Please contact your administrator.");
		}
	}	

}
