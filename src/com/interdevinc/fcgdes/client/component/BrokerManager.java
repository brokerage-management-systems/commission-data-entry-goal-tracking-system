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
import com.interdevinc.fcgdes.client.process.broker.BrokerManagerService;
import com.interdevinc.fcgdes.client.process.broker.BrokerManagerServiceAsync;

public class BrokerManager extends Manager implements ClickHandler, ChangeHandler {

	@SuppressWarnings("unused")
	private FCG mainModule;

	private Button addButton, editButton, clearButton;
	private HorizontalPanel buttonPanel;

	private FlexTable informationTable;
	private TextBox numberBox;
	private TextBox firstnameBox, lastnameBox;
	private TextBox emailBox;
	private ListBox statusBox;

	private TextBoxBase boxes[];

	private BrokerManagerServiceAsync brokerService;
	private InsertUpdateHandler insertUpdateHandler;

	private ManagerView container;

	public BrokerManager(String t){
		super(t);
		initAllComponents();
		assembleComponents();
		initRemoteProcedureWorkers();
	}

	/**
	 * METHOD:	CLEAR FORM FIELDS	 */
	public void clearFormFields(){
		for(TextBoxBase t:boxes){
			t.setText("");
		}
	}

	/**
	 * GET STATUS
	 * @return - listbox status	 */
	public boolean getStatus(){
		return (statusBox.getSelectedIndex()==0)?true:false;
	}

	/**
	 * METHOD:	ON CHANGE
	 * For listbox events	 */
	public void onChange(ChangeEvent event) {

		if(event.getSource()==getListBoxInstance()){
			if(getListBoxIndex()==-1){
				clearFormFields();
			}else{
				editButton.setEnabled(true);
				updateFormFields(getBrokerAtCurrentIndex());
			}
		}
	}

	/**
	 * METHOD:	ON CLICK	 */
	public void onClick(ClickEvent event) {

		editButton.setEnabled(false);

		if(event.getSource()==addButton){

			//if valid data
			if(containsValidFormData()){

				//create broker from fields
				Broker broker=createBrokerFromFieldData();
				brokerService.insertBroker(broker,insertUpdateHandler);

			}else{
				Window.alert("Please fill out ALL form fields.");
			}

		}else if(event.getSource()==editButton){

			//create broker from field
			Broker broker=updateBrokerFromFieldData(getBrokerAtCurrentIndex());
			brokerService.updateBroker(broker,insertUpdateHandler);

		}else if(event.getSource()==clearButton){
			//clear all form data
			clearFormFields();
		}
	}

	/**
	 * METHOD:	SET CONTAINER
	 * Sets container for external manipulation	 */
	public void setContainer(ManagerView view) {
		container=view;
	}

	public void setMainModule(FCG main){
		mainModule=main;
	}

	/**
	 * METHOD:	ASSEMBLE COMPONENTS	 */
	private void assembleComponents(){

		informationTable.setWidget(0,0,new Label("Rep. #"));
		informationTable.setWidget(0,1,numberBox);			

		informationTable.setText(1,1,"First");
		informationTable.setText(1,2,"Last");

		informationTable.setWidget(2,0,new Label("Name"));
		informationTable.setWidget(2,1,firstnameBox);
		informationTable.setWidget(2,2,lastnameBox);

		//	informationTable.setWidget(3,0,new Label("Email"));
		//	informationTable.setWidget(3,1,emailBox);
		//	informationTable.getFlexCellFormatter().setColSpan(3,1,2);

		informationTable.setWidget(3,0,new Label("Status"));
		informationTable.setWidget(3,1,statusBox);
		informationTable.getFlexCellFormatter().setColSpan(3,1,2);

		add(informationTable);

		buttonPanel.setSpacing(5);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(clearButton);

		add(buttonPanel);
	}

	/**
	 * METHOD:	CHECK VALID FORM DATA
	 * @return - true or false		 */
	private boolean containsValidFormData(){
		//iterate and check
		for(TextBoxBase b:boxes){
			if((b.getValue().trim()).equals("") || b.getValue()==null){
				return false;
			}
		}
		return true;
	}

	/**
	 * METHOD:	CREATE BROKER FROM FIELD DATA
	 * @return - new broker instance	 */
	private Broker createBrokerFromFieldData(){
		return new Broker(0,numberBox.getText(),firstnameBox.getText(),lastnameBox.getText(),getStatus());
	}

	/**
	 * METHOD:	INIT ALL COMPONENTS	 */
	private void initAllComponents(){

		//init all textboxes
		numberBox=new TextBox();
		numberBox.setWidth("60px");

		firstnameBox=new TextBox();
		firstnameBox.setWidth("80px");

		lastnameBox=new TextBox();
		lastnameBox.setWidth("80px");

		emailBox=new TextBox();
		emailBox.setWidth("170px");

		statusBox=new ListBox();
		statusBox.addItem("Active");
		statusBox.addItem("Inactive");
		statusBox.setWidth("100px");

		//polymorphic for easy iteration
		boxes=new TextBoxBase[]{numberBox,firstnameBox,lastnameBox};

		//init and add listeners
		addButton=new Button("Add");
		addButton.addClickHandler(this);

		editButton=new Button("Update");
		editButton.addClickHandler(this);
		editButton.setEnabled(false);

		clearButton=new Button("Clear");
		clearButton.addClickHandler(this);

		//init panel for buttons
		buttonPanel=new HorizontalPanel();

		//init table for form fields
		informationTable=new FlexTable();

		//add this native listener to super widget
		getListBoxInstance().addChangeHandler(this);
	}

	/**
	 * METHOD:	INIT REMOTE PROCEDURE WORKERS	 */
	private void initRemoteProcedureWorkers(){

		//insert service
		brokerService=(BrokerManagerServiceAsync)GWT.create(BrokerManagerService.class);

		//init handler
		insertUpdateHandler=new InsertUpdateHandler();
	}

	/**
	 * METHOD:	UPDATE BROKER FROM FIELD DATA
	 * @param broker - broker to modify
	 * @return - modified broker from parameter	 */
	private Broker updateBrokerFromFieldData(Broker broker){

		broker.setRepNumber(numberBox.getText());
		broker.setFirstname(firstnameBox.getText());
		broker.setLastname(lastnameBox.getText());
		broker.setStatus(getStatus());

		return broker;
	}

	/**
	 * METHOD:	UPDATE FORM FIELDS
	 * This method takes a broker object and populates
	 * the corresponding form fields with the associating
	 * data
	 * @param b - broker object to fill form fields	 */
	private void updateFormFields(Broker b){
		numberBox.setText(b.getRepNumber());
		firstnameBox.setText(b.getFirstname());
		lastnameBox.setText(b.getLastname());
		emailBox.setText(b.getEmail());
		statusBox.setSelectedIndex((b.getStatus())?0:1);
	}

	/**
	 * 	PRIVATE CLASS:	INSERT HANDLER
	 * 	@author floresj4
	 *	Inserts a new broker and rebuilds all lists on success	 */
	private class InsertUpdateHandler implements AsyncCallback<Boolean>{

		/**
		 * METHOD:	ON SUCCESS		 */
		public void onSuccess(Boolean success){
			//update ALL lists within ALL managers and clear fields
			container.updateAllBrokerLists();

			DialogBox brokerSuccess = new DialogBox();
			brokerSuccess.setGlassEnabled(true);
			brokerSuccess.setAnimationEnabled(true);
			brokerSuccess.setAutoHideEnabled(true);
			brokerSuccess.setTitle("Add/Update Broker");

			if (success) {
				brokerSuccess.add(new HTML("<p>Broker has been added/updated.</p>"));
			} else {
				brokerSuccess.add(new HTML("<p>There was a problem adding/updating the Broker.</p><p>Please make sure the Rep Number you supplied does not belong to an existing Broker</p>"));
			}

			brokerSuccess.center();

			clearFormFields();
		}

		/**
		 * METHOD:	ON FAILURE		 */
		public void onFailure(Throwable ex){

		}
	}
}