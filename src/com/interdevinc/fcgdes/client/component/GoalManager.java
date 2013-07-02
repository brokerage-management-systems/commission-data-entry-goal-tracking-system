package com.interdevinc.fcgdes.client.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.interdevinc.fcgdes.client.FCG;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.process.goal.GoalManagerService;
import com.interdevinc.fcgdes.client.process.goal.GoalManagerServiceAsync;

public class GoalManager extends Manager implements ClickHandler, ChangeHandler {

	@SuppressWarnings("unused")
	private FCG mainModule;

	@SuppressWarnings("unused")
	private ManagerView container;

	private ListBox goalYearBox;
	private TextBox totalGrossBox;
	private TextBox moneyRaisedBox;
	private TextBox newAccountsBox;

	private TextBoxBase boxes[];

	private Button addButton;
	private Button editButton;

	private FlexTable goalTable;
	@SuppressWarnings("unused")
	private FlexTable editGoalTable;

	private GoalManagerServiceAsync goalService;
	private InsertHandler insertHandler;
	private RetrieveHandler retrieveHandler;
	private UpdateHandler updateHandler;

	private ListBox repSelectionBox;

	public GoalManager(String t){
		super(t);

		//initialize and assemble all components
		initAllComponents();
		assembleComponents();
		initRemoteProcedureWorkers();

	}

	/**
	 * METHOD:	ON CHANGE	 */
	public void onChange(ChangeEvent ce){

		//clear all fields
		//clearAllFields();

		if(!(repSelectionBox.getSelectedIndex()==0)){

			//get current broker and year
			Broker broker=getBrokerAtCurrentIndex();
			String goalYear=goalYearBox.getValue(goalYearBox.getSelectedIndex());

			if((ce.getSource()==repSelectionBox) || (ce.getSource()==goalYearBox)){

				// clear text boxes
				clearAllFields();

				//attempt to retrieve goal data
				goalService.retrieveBrokerGoal(broker.getRepNumber(),goalYear,retrieveHandler);
			}
		}

		// Check Goal Textboxes for numeric values.
		if (ce.getSource()==totalGrossBox) {
			if (!totalGrossBox.getText().matches("[0-9.]*")) {
				totalGrossBox.setText(null);
				Window.alert("Only Numeric Characters are allowed in this field.");
			}
		}
		if (ce.getSource()==moneyRaisedBox) {
			if (!moneyRaisedBox.getText().matches("[0-9.]*")) {
				moneyRaisedBox.setText(null);
				Window.alert("Only Numeric Characters are allowed in this field.");
			}
		}
		if (ce.getSource()==newAccountsBox) {
			if (!newAccountsBox.getText().matches("[0-9.]*")) {
				newAccountsBox.setText(null);
				Window.alert("Only Numeric Characters are allowed in this field.");
			}
		}
	}

	/**
	 * METHOD:	ON CLICK	 */
	public void onClick(ClickEvent ce){

		//if 'addButton'
		if(ce.getSource()==addButton){

			goalService.insertGoal(createGoalFromFields(),insertHandler);

			//if 'editButton'
		}else if(ce.getSource()==editButton){

			goalService.updateGoal(createGoalFromFields(),updateHandler);

		}
	}

	/**
	 * METHOD:	SET CONTAINER
	 * Sets container for external manipulation	 */
	public void setContainer(ManagerView view) {
		container=view;
	}

	/**
	 * METHOD:	SET MAIN MODULE	 */
	public void setMainModule(FCG main){
		mainModule=main;
	}

	/**
	 * METHOD:	ASSEMBLE COMPONENTS	 */
	private void assembleComponents(){
		add(goalTable);
	}

	/**
	 * METHOD:	CLEAR ALL FIELDS	 */
	private void clearAllFields(){
		//iterate and clear
		for(TextBoxBase b:boxes){
			b.setText("");
		}
		//disable buttons
		addButton.setEnabled(false);
		editButton.setEnabled(false);
	}

	/**
	 * METHOD:	CREATE GOAL FROM FIELDS
	 * @return - Goal instance	 */
	private Goal createGoalFromFields(){
		//retrieve values
		String repNumber=getBrokerAtCurrentIndex().getRepNumber();
		String goalYear=goalYearBox.getValue(goalYearBox.getSelectedIndex());
		String moneyRaised=moneyRaisedBox.getText();
		String newAccounts=newAccountsBox.getText();
		String totalGross=totalGrossBox.getText();

		return new Goal(repNumber,goalYear,moneyRaised,newAccounts,totalGross);
	}

	/**
	 * METHOD:	INIT ALL COMPONENTS	 */
	private void initAllComponents(){

		//get associated instance
		repSelectionBox=getListBoxInstance();
		repSelectionBox.addChangeHandler(this);

		//init all textboxes
		goalYearBox=new ListBox();
		goalYearBox.addItem("2010");
		goalYearBox.addItem("2011");
		goalYearBox.addItem("2012");
		goalYearBox.addItem("2013");

		totalGrossBox=new TextBox();
		moneyRaisedBox=new TextBox();
		newAccountsBox=new TextBox();
		totalGrossBox.addChangeHandler(this);
		moneyRaisedBox.addChangeHandler(this);
		newAccountsBox.addChangeHandler(this);
		boxes=new TextBoxBase[]{totalGrossBox,moneyRaisedBox,newAccountsBox};

		addButton=new Button("Add");
		addButton.addClickHandler(this);
		addButton.setEnabled(false);

		editButton=new Button("Update");
		editButton.addClickHandler(this);
		editButton.setEnabled(false);

		//labels
		final String labels[]={"Goal Year","Total Gross","Money Raised","New Accounts"};

		//init table
		goalTable=new FlexTable();

		//iterate and build
		for(int x=0; x<labels.length; x++){
			goalTable.setWidget(x,0,new Label(labels[x]));
		}

		//add text boxes
		goalTable.setWidget(0,1,goalYearBox);
		goalTable.setWidget(1,1,totalGrossBox);
		goalTable.setWidget(2,1,moneyRaisedBox);
		goalTable.setWidget(3,1,newAccountsBox);

		HorizontalPanel buttonPanel=new HorizontalPanel();
		buttonPanel.setSpacing(5);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);

		//add button and span
		goalTable.setWidget(4,1,buttonPanel);
		goalTable.getFlexCellFormatter().setStyleName(4,0,"form-button-row");
	}	

	/**
	 * METHOD:	INIT REMOTE PROCEDURE WORKERS	 */
	private void initRemoteProcedureWorkers(){
		//set service endpoint
		goalService=(GoalManagerServiceAsync)GWT.create(GoalManagerService.class);

		//init RPC handler
		insertHandler=new InsertHandler();
		retrieveHandler=new RetrieveHandler();
		updateHandler=new UpdateHandler();
	}

	/**
	 * METHOD:	UPDATE GOAL FIELDS
	 * @param g	 */
	private void updateGoalFields(Goal g){
		//set values
		totalGrossBox.setText(g.getTotalGross());
		moneyRaisedBox.setText(g.getMoneyRaised());
		newAccountsBox.setText(g.getNewAccounts());
	}

	/**
	 * PRIVATE CLASS:	INSERT HANDLER
	 * @author floresj4	 */
	private class InsertHandler implements AsyncCallback<Boolean>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Boolean result) {
			DialogBox insertSuccess = new DialogBox();
			insertSuccess.setGlassEnabled(true);
			insertSuccess.setAnimationEnabled(true);
			insertSuccess.setAutoHideEnabled(true);
			insertSuccess.setTitle("Add Goal");
			if (result) {
				insertSuccess.add(new HTML("<p>The Goal has been added.</p>"));
			} else {
				// 
				insertSuccess.add(new HTML("<p>There was a problem adding the Goal.</p><p>Please make sure a Goal does not exist for the same Rep Number & Goal Year.</p>"));
			}
			insertSuccess.center();
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable caught) {

		}	
	}

	/**
	 * PRIVATE CLASS:	RETRIEVE HANDLER
	 * @author floresj4	 */
	private class RetrieveHandler implements AsyncCallback<Goal>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Goal goal) {
			if(goal!=null){
				//update to display
				updateGoalFields(goal);
				editButton.setEnabled(true);
			}else{
				addButton.setEnabled(true);
			}
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable caught) {

		}	
	}


	/**
	 * PRIVATE CLASS:	UPDATE HANDLER
	 * @author floresj4	 */
	private class UpdateHandler implements AsyncCallback<Boolean>{
		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Boolean result) {
			DialogBox updateSuccess = new DialogBox();
			updateSuccess.setGlassEnabled(true);
			updateSuccess.setAnimationEnabled(true);
			updateSuccess.setAutoHideEnabled(true);
			updateSuccess.setTitle("Update Goal");
			if (result) {
				updateSuccess.add(new HTML("<p>The Goal has been updated.</p>"));
			} else {
				// 
				updateSuccess.add(new HTML("<p>There was a problem updating the Goal.</p>"));
			}
			updateSuccess.center();
			//clear fields
			clearAllFields();
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable caught) {

		}		
	}
}