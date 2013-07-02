package com.interdevinc.fcgdes.client.component;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.interdevinc.fcgdes.client.FCG;
import com.interdevinc.fcgdes.client.component.widget.FormattedDateBox;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Gross;
import com.interdevinc.fcgdes.client.process.gross.GrossManagerService;
import com.interdevinc.fcgdes.client.process.gross.GrossManagerServiceAsync;

public class GrossManager extends Manager implements ClickHandler{

	private ManagerView container;
	private FCG mainModule;

	private HorizontalPanel insertEditPanel;
	private FlexTable informationTable;

	//text boxes for Gross Data.
	private TextBox nfsGrossBox;
	private TextBox legentGrossBox;
	private TextBox dealGrossBox;
	private TextBox insGrossBox;
	private TextBox moneyRaisedBox;
	private TextBox newAccountBox;
	private TextBox renegBox;

	private TextBoxBase boxes[];
	private Button submitButton;

	private DialogBox confirmBox;
	private HTML confirmText;
	private Button confirmButton, cancelButton;

	//edit fields
	private FlexTable updateTable;
	private final int UPDATE_START_INDEX=4;

	private FormattedDateBox grossDateBox;
	private FormattedDateBox updateStartDate;
	private FormattedDateBox updateEndDate;

	private Broker currentBroker;
	private GrossManagerServiceAsync grossService;
	private InsertHandler insertHandler;
	private UpdateHandler updateHandler;
	private RetrieveHandler retrieveHandler;

	private Button viewButton;
	private Button editButtons[];

	private UpdateDialog updateDialog;

	public GrossManager(String t){
		super(t);

		//initialize and assemble all components
		initAllComponents();
		assembleComponents();
		initRemoteProcedureWorkers();
	}

	/**
	 * METHOD:	ON CLICK	 */
	public void onClick(ClickEvent ce){

		if(ce.getSource()==submitButton){

			//return on no user selected
			if(getListBoxIndex()==-1){
				Window.alert("Please select a Broker to attach Gross information to");
				return;
			}
			//get current broker and set gross data
			currentBroker=getBrokerAtCurrentIndex();
			currentBroker.setGross(createGrossFromFields());

			//set text for confirmation
			confirmText.setHTML(currentBroker.toHTMLString());
			confirmBox.setAnimationEnabled(true);
			confirmBox.setGlassEnabled(true);
			confirmBox.center();
			confirmBox.show();

		}else if(ce.getSource()==viewButton){

			//if valid broker
			if(!(getListBoxIndex()==-1)){

				//if both fields are filled properly
				if(!(((updateStartDate.getText()).equals("")) && ((updateEndDate.getText()).equals("")))){

					//retrieve date values
					String startDate=updateStartDate.getText();
					String endDate=updateEndDate.getText();

					//determine valid date range
					if(determineValidDateRange(startDate,endDate)){

						//get current Broker and execute
						Broker currentBroker=getBrokerAtCurrentIndex();
						grossService.retrieveGrossDataRange(currentBroker.getRepNumber(),startDate,endDate,retrieveHandler);
					}else{
						Window.alert("Please enter a valid date range");
					}

				}else{
					Window.alert("Please fill both date range fields");
				}

			}else{
				Window.alert("Please select a broker.");
			}

		}else if(ce.getSource()==confirmButton){

			grossService.insertGrossData(currentBroker,insertHandler);

		}else if(ce.getSource()==cancelButton){

			confirmBox.hide();
		}
	}

	/**
	 * METHOD:	SET CONTAINER
	 * Sets container for external manipulation	 */
	public void setContainer(ManagerView view) {
		container=view;
	}

	/**
	 * METHOD:	SET MAIN MODULE
	 * Sets the Entry point for logic controls	 */
	public void setMainModule(FCG main){
		mainModule=main;
	}

	/**
	 * METHOD:	ASSEMBLE COMPONENTS	 */
	private void assembleComponents(){

		informationTable.setWidget(0,0,new HTML("<h3>Add Gross</h3>"));
		informationTable.getFlexCellFormatter().setColSpan(0,0,2);

		//add all labels
		informationTable.setWidget(1,0,new Label("Gross Date"));
		informationTable.setWidget(2,0,new Label("NFS"));
		informationTable.setWidget(3,0,new Label("Legent"));
		informationTable.setWidget(4,0,new Label("Deal"));
		informationTable.setWidget(5,0,new Label("Insurance"));
		informationTable.setWidget(6,0,new Label("Money Raised"));
		informationTable.setWidget(7,0,new Label("New Accounts"));
		informationTable.setWidget(8,0,new Label("Renegs"));

		//add all boxes
		informationTable.setWidget(1,1,grossDateBox);
		informationTable.setWidget(2,1,nfsGrossBox);
		informationTable.setWidget(3,1,legentGrossBox);
		informationTable.setWidget(4,1,dealGrossBox);
		informationTable.setWidget(5,1,insGrossBox);
		informationTable.setWidget(6,1,moneyRaisedBox);
		informationTable.setWidget(7,1,newAccountBox);
		informationTable.setWidget(8,1,renegBox);

		//add button, set span and style
		informationTable.setWidget(9,0,submitButton);
		informationTable.getFlexCellFormatter().setColSpan(9,0,2);
		informationTable.getFlexCellFormatter().setStylePrimaryName(9,0,"form-button-row");

		insertEditPanel.add(informationTable);

		updateTable.setWidget(0,0,new HTML("<h3>Edit Gross</h3>"));
		updateTable.getFlexCellFormatter().setColSpan(0,0,2);

		updateTable.setWidget(1,0,new Label("Start Date"));
		updateTable.setWidget(1,1,new Label("End Date"));
		updateTable.setWidget(2,0,updateStartDate);
		updateTable.setWidget(2,1,updateEndDate);
		updateTable.setWidget(2,2,viewButton);

		updateTable.getFlexCellFormatter().setColSpan(1,0,2);
		updateTable.getFlexCellFormatter().setColSpan(2,0,2);
		updateTable.getFlexCellFormatter().setColSpan(2,1,2);

		insertEditPanel.add(updateTable);

		//add to Manager
		add(insertEditPanel);
	}

	/**
	 * METHOD:	BUILD GROSS DATA TABLE	 */
	private void buildGrossDataTable(final ArrayList<Gross> grossList){

		//build table header
		buildTableHeader();

		//dynamically set number of buttons
		editButtons=new Button[grossList.size()];

		//iterate and build
		int row=(UPDATE_START_INDEX+1);
		for(int x = 0; x<grossList.size(); x++){

			//shade every other
			if(x%2==0){
				updateTable.getRowFormatter().setStyleName(row,"shaded");
			}

			//get current gross
			Gross currentGross=grossList.get(x);

			//set column data
			updateTable.setText(row,0,currentGross.getGrossDate());
			updateTable.setText(row,1,currentGross.getNFSGross());
			updateTable.setText(row,2,currentGross.getLegentGross());
			updateTable.setText(row,3,currentGross.getDealGross());
			updateTable.setText(row,4,currentGross.getInsuranceGross());
			updateTable.setText(row,5,currentGross.getMoneyRaised());
			updateTable.setText(row,6,currentGross.getNewAccount());
			updateTable.setText(row,7,currentGross.getReneg());

			//init button and add listener
			editButtons[x]=new GrossEditButton("Edit",x);
			editButtons[x].addClickHandler(new ClickHandler(){

				/**
				 * METHOD:	ON CLICK	*/
				public void onClick(ClickEvent ce){
					//get source and gross from source
					GrossEditButton button=(GrossEditButton)ce.getSource();
					Gross g=grossList.get(button.getIndex());

					//set Gross for dialog and show
					updateDialog.updateVisibleGrossData(g);
					updateDialog.setAnimationEnabled(true);
					updateDialog.setGlassEnabled(true);
					updateDialog.center();
					updateDialog.show();
				}
			});

			//add button to row
			updateTable.setWidget(row,8,editButtons[x]);

			//style particular cells
			updateTable.getFlexCellFormatter().setStyleName(row,0,"center");
      //updateTable.getFlexCellFormatter().setStyleName(row,1,"right");
      //updateTable.getFlexCellFormatter().setStyleName(row,2,"right");
      //updateTable.getFlexCellFormatter().setStyleName(row,3,"right");
      //updateTable.getFlexCellFormatter().setStyleName(row,4,"right");
      //updateTable.getFlexCellFormatter().setStyleName(row,5,"right");
			updateTable.getFlexCellFormatter().setStyleName(row,6,"center");
			updateTable.getFlexCellFormatter().setStyleName(row,7,"center");

			//increment row
			row++;
		}
	}

	/**
	 * METHOD:	BUILD TABLE HEADER	 */
	private void buildTableHeader(){

		//table header titles
		final String tableHeader[]={"Date","NFS","Legent","Deal","Ins.","$$ Raised","Acct","Reneg"};

		//iterate and build
		for(int x=0; x<tableHeader.length; x++){
			//set label and style
			updateTable.setText(3,x,tableHeader[x]);
			updateTable.getFlexCellFormatter().setStyleName(3,x,"table-header");
		}
	}

	/**
	 * METHOD:	INIT ALL COMPONENTS	 */
	private void initAllComponents(){

		//init dialog for updates
		updateDialog=new UpdateDialog();

		//init insert and edit panel
		insertEditPanel=new HorizontalPanel();
		insertEditPanel.setSpacing(10);

		//init table
		informationTable=new FlexTable();

		//init date box
		grossDateBox=new FormattedDateBox();

		//init all textboxes
		nfsGrossBox=new TextBox();
		legentGrossBox=new TextBox();
		dealGrossBox=new TextBox();
		insGrossBox=new TextBox();
		moneyRaisedBox=new TextBox();

		newAccountBox=new TextBox();
		newAccountBox.setWidth("60px");

		renegBox=new TextBox();
		renegBox.setWidth("60px");

		boxes=new TextBoxBase[]{nfsGrossBox,legentGrossBox,dealGrossBox,insGrossBox,moneyRaisedBox,newAccountBox,renegBox};

		confirmText=new HTML();
		confirmText.setStylePrimaryName("confirmation-text");

		confirmButton=new Button("Confirm");
		confirmButton.addClickHandler(this);

		cancelButton=new Button("Cancel");
		cancelButton.addClickHandler(this);		

		final HorizontalPanel buttonPanel=new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);

		final VerticalPanel confirmLayout=new VerticalPanel();
		confirmLayout.setSpacing(10);
		confirmLayout.add(confirmText);
		confirmLayout.add(buttonPanel);

		confirmBox=new DialogBox();
		confirmBox.setText("Confirm Information");
		confirmBox.add(confirmLayout);

		//init buttons and listener
		submitButton=new Button("Insert Gross");
		submitButton.addClickHandler(this);

		//init edit fields table
		updateTable=new FlexTable();
		updateTable.setCellSpacing(0);
		updateTable.setCellPadding(2);

		viewButton=new Button("View");
		viewButton.addClickHandler(this);

		updateStartDate=new FormattedDateBox();
		updateEndDate=new FormattedDateBox();
	}

	/**
	 * METHOD:	INIT REMOTE PROCEDURE WORKERS	 */
	private void initRemoteProcedureWorkers(){
		//set service endpoint
		grossService=(GrossManagerServiceAsync)GWT.create(GrossManagerService.class);

		//initialize RPC handlers
		insertHandler=new InsertHandler();
		updateHandler=new UpdateHandler();
		retrieveHandler=new RetrieveHandler();
	}

	/**
	 * METHOD:	CLEAR FORM FIELDS	 */
	private void clearFormFields(){

		//clear date box
		grossDateBox.getTextBox().setText("");

		//iterate and clear
		for(TextBoxBase tb:boxes){
			tb.setText("");
		}
	}

	/**
	 * METHOD:	CLEAR ROW DATA	 */
	private void clearRowData(){
		//iterate and remove rows
		for(int x=updateTable.getRowCount(); x>UPDATE_START_INDEX; x--){
			updateTable.removeRow((x-1));
		}
	}

	/**
	 * METHOD:	CREATE GROSS FROM FIELDS
	 * @return - new Gross instance from form fields	 */
	private Gross createGrossFromFields(){

		String gd = isNumeric(grossDateBox.getText(), "double");
		String dl = isNumeric(dealGrossBox.getText(), "double");
		String ig = isNumeric(insGrossBox.getText(), "double");
		String lg = isNumeric(legentGrossBox.getText(), "double");
		String mr = isNumeric(moneyRaisedBox.getText(), "double");
		String na = isNumeric(newAccountBox.getText(), "int");
		String ng = isNumeric(nfsGrossBox.getText(), "double");
		String re = isNumeric(renegBox.getText(), "int");

		return new Gross(gd,dl,ig,lg,mr,na,ng,re);
	}

	/**
	 * METHOD:	DETERMINE VALID DATE RANGE	 */
	private boolean determineValidDateRange(String start, String end){

		//create array for independent testing
		//[0]=year	[1]=month	[2]=day
		int startData[]=new int[]{Integer.parseInt(start.substring(0,4)),Integer.parseInt(start.substring(4,6)),
				Integer.parseInt(start.substring(6,8))};

		int endData[]=new int[]{Integer.parseInt(end.substring(0,4)),Integer.parseInt(end.substring(4,6)),
				Integer.parseInt(end.substring(6,8))};

		//if the end year is before the start year
		if(endData[0]<startData[0]){
			return false;
		}

		//if the end month is
		if(endData[1]<startData[1]){
			return false;
		}

		if((endData[1]==startData[1]) && (endData[2]<startData[2])){
			return false;
		}

		return true;
	}

	/**
	 * METHOD: IS NUMERIC
	 * @param stringValue
	 * @return - If string is not numeric returns "0" otherwise returns the original string.
	 */
	private String isNumeric(String stringValue, String type) {

		String numericValue;

		if (stringValue.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {  
			numericValue = stringValue;
		} else {
			if (type.equals("double")) {
				numericValue = "0.00";
			} else if (type.equals("int")) {
				numericValue = "0";
			} else {
				numericValue = "0";
			}
		}

		return numericValue;
	}

	/**
	 *	PRIVATE CLASS:	GROSS EDIT BUTTON 
	 * @author floresj4	 */
	private class GrossEditButton extends Button{

		private int index;

		public GrossEditButton(String title, int i){
			super(title);
			setIndex(i);
		}

		/**
		 * METHOD:	GET INDEX
		 * @return - associating index		 */
		public int getIndex(){
			return index;
		}

		/**
		 * METHOD:	SET INDEX
		 * @param i		 */
		public void setIndex(int i){
			index=i;
		}
	}

	/**
	 * 	PRIVATE CLASS UPDATE DIALOG
	 * @author floresj4	 */
	private class UpdateDialog extends DialogBox implements ClickHandler{

		final String labels[]={"Date","NFS","Legent","Deal","Insurance","Money Raised","New Accounts","Reneg(s)"};

		private FlexTable table;

		private TextBox boxes[];

		private Button updateButton;
		private Button cancelUpdate;

		private String currentID=null;

		public UpdateDialog(){

			//set dialog title
			setTitle("Update Gross Data");

			//initialize components
			initAllComponents();

			//init table
			table=new FlexTable();

			//iterate and add labels
			for(int x=0; x<labels.length; x++){
				table.setWidget(x,0,new Label(labels[x]));
				table.setWidget(x,1,boxes[x]);
			}

			//set button on the last table-row
			table.setWidget(labels.length,0,updateButton);
			table.setWidget(labels.length,1,cancelUpdate);
			//table.getFlexCellFormatter().setColSpan(labels.length,0,2);
			table.getFlexCellFormatter().setStyleName(labels.length,0,"form-button-row");
			table.getFlexCellFormatter().setStyleName(labels.length,1,"form-button-row");

			//add table to dialog
			add(table);
		}

		/**
		 * METHOD:	ON CLICK		 */
		public void onClick(ClickEvent ce){

			if (ce.getSource().equals(updateButton)) {
				//execute RPC and 'hide' dialogbox
				grossService.updateGrossData(createGrossFromFields(),updateHandler);
				//hide 'this'
				hide();
			}

			if (ce.getSource().equals(cancelUpdate)) {
				hide();
			}
		}

		/**
		 * METHOD:	INIT ALL COMPONENTS		 */
		private void initAllComponents(){
			//init array
			boxes=new TextBox[8];

			//iterate and init
			for(int x=0; x<boxes.length; x++){
				//init box
				boxes[x]=new TextBox();

				//disable date field
				if(x==0){
					boxes[x].setEnabled(false);
				}
			}

			//init update button and handler
			updateButton=new Button("Update");
			updateButton.addClickHandler(this);

			//init cancel update and handler
			cancelUpdate=new Button("Cancel");
			cancelUpdate.addClickHandler(this);
		}

		/**
		 * METHOD:	UPDATE VISIBLE GROSS DATA
		 * @param g		 */
		public void updateVisibleGrossData(Gross g){
			//set the current ID
			currentID=g.getGrossID();

			//set text values
			boxes[0].setText(g.getGrossDate());
			boxes[1].setText(g.getNFSGross());
			boxes[2].setText(g.getLegentGross());
			boxes[3].setText(g.getDealGross());
			boxes[4].setText(g.getInsuranceGross());
			boxes[5].setText(g.getMoneyRaised());
			boxes[6].setText(g.getNewAccount());
			boxes[7].setText(g.getReneg());
		}

		/**
		 * METHOD:	CREATE GROSS FROM FIELDS
		 * @return - new Gross instance from form fields	 */
		private Gross createGrossFromFields(){

			String gd=boxes[0].getText();
			String ng=boxes[1].getText();
			String lg=boxes[2].getText();
			String dl=boxes[3].getText();
			String ig=boxes[4].getText();
			String mr=boxes[5].getText();
			String na=boxes[6].getText();
			String re=boxes[7].getText();

			return new Gross(currentID,gd,dl,ig,lg,mr,na,ng,re);
		}
	}

	/**
	 * 	PRIVATE CLASS:	INSERT HANDLER
	 * 	This class is designed to handle the success and 
	 * 	failure scenarios for inserting broker gross data
	 * 	@author floresj4	 */
	private class InsertHandler implements AsyncCallback<Boolean>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Boolean b){

			confirmBox.hide();

			//clear all form fields
			clearFormFields();
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable t){

		}
	}

	/**
	 * 	PRIVATE CLASS:	UPDATE HANDLER
	 * This class is designed to handle the success and 
	 * failure scenarios for retrieving broker gross data
	 * @author floresj4	 */	
	private class RetrieveHandler implements AsyncCallback<ArrayList<Gross>>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(ArrayList<Gross> grossList){

			//clear previous data
			clearRowData();

			//if no data returned
			if(grossList.size()==0){

				//display no results found
				updateTable.setHTML((UPDATE_START_INDEX),0,"<p class='error-label'>No Gross data found.</p>");
				updateTable.getFlexCellFormatter().setColSpan(UPDATE_START_INDEX,0,6);
				return;
			}

			//build table with results
			buildGrossDataTable(grossList);
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable t){

		}
	}

	/**
	 * 	PRIVATE CLASS:	UPDATE HANDLER
	 * This class is designed to handle the success and 
	 * failure scenarios for updating broker gross data
	 * @author floresj4	 */	
	private class UpdateHandler implements AsyncCallback<Boolean>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Boolean b){
			//clear data
			clearRowData();

			//get current Broker
			Broker currentBroker=getBrokerAtCurrentIndex();

			//get start and end dates
			String startDate=updateStartDate.getText();
			String endDate=updateEndDate.getText();

			//execute retrieval
			grossService.retrieveGrossDataRange(currentBroker.getRepNumber(),startDate,endDate,retrieveHandler);
		}

		public void onFailure(Throwable t){

		}
	}
}
