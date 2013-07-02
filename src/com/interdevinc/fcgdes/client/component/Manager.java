package com.interdevinc.fcgdes.client.component;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.process.broker.BrokerManagerService;
import com.interdevinc.fcgdes.client.process.broker.BrokerManagerServiceAsync;

public abstract class Manager extends VerticalPanel implements Containable{

	private ArrayList<Broker> brokerList;

	private ListBox brokerListBox;
	private HorizontalPanel repSelectionPanel;

	private BrokerManagerServiceAsync brokerService;
	private BrokerListHandler brokerHandler;

	public Manager(String t){
		setTitle(t);

		buildBrokerListWidget();
		initRemoteProcedureWorkers();
	}

	/**
	 * METHOD:	GET BROKER AT
	 * @param x - index within broker list
	 * @return - Broker	 */
	public Broker getBrokerAt(int x){
		return brokerList.get(x);
	}

	/**
	 * METHOD:	GET BROKER AT CURRENT INDEX
	 * @return - broker instance at current list box index	 */
	public Broker getBrokerAtCurrentIndex(){
		return brokerList.get(getListBoxIndex());
	}

	/**
	 * METHOD:	GET LIST BOX INDEX
	 * @return - the currently select list box index	 */
	protected int getListBoxIndex(){
		return (brokerListBox.getSelectedIndex()-1); 
	}

	/**
	 * METHOD:	GET LIST BOX INSTANCE
	 * @return - an instance of the brokerListBox
	 * to be used by inheriting classes	 */
	protected ListBox getListBoxInstance(){
		return brokerListBox;
	}

	/**
	 * METHOD:	GET SELECTED REP NUMBER
	 * @return - the currently selected rep's number	 */
	protected String getSelectedRepNumber(){
		return brokerList.get(getListBoxIndex()).getRepNumber();
	}

	/**
	 * METHOD:	SET BROKER LIST
	 * @param b - list of brokers to set	 */
	protected void setBrokerList(ArrayList<Broker> b) {
		brokerList=b;
	}

	/**
	 * METHOD:	TOGGLE REP SELECTION AVAILABILITY
	 * @param availability	 */
	protected void toggleRepSelectionAvailability(boolean availability){
		if(availability){
			add(repSelectionPanel);
		}else{
			remove(repSelectionPanel);
		}
	}

	/**
	 * METHOD:	TOGGLE REP SELECTION VISIBILITY
	 * @param visible	 */
	protected void toggleRepSelectionVisibility(boolean visible){
		repSelectionPanel.setVisible(visible);
	}

	/**
	 * METHOD:	UPDATE BROKER LIST
	 * This method clears and repopulates the broker list
	 * widget	 */
	protected void updateBrokerList(){
		brokerListBox.clear();
		brokerListBox.addItem("Select Broker");

		brokerService.retrieveBrokerList(brokerHandler);
	}

	/**
	 * METHOD:	BUILD LIST WIDGET
	 * @param brokerList - list of brokers retrieve from Database	 */
	private void buildBrokerListWidget(){

		//init brokerList widget
		brokerListBox = new ListBox();
		brokerListBox.setWidth("165px");
		brokerListBox.addItem("Select Broker");

		repSelectionPanel=new HorizontalPanel();
		repSelectionPanel.setSpacing(5);
		repSelectionPanel.add(new Label("Representative"));
		repSelectionPanel.add(brokerListBox);

		add(repSelectionPanel);
	}

	/**
	 * METHOD:	INIT REMOTE PROCEDURE WORKER	 */
	private void initRemoteProcedureWorkers(){
		brokerService=(BrokerManagerServiceAsync)GWT.create(BrokerManagerService.class);

		//init handler
		brokerHandler=new BrokerListHandler();
	}

	/**
	 * PRIVATE CLASS:	BROKER LIST HANDLER
	 * @author floresj4	 */
	private class BrokerListHandler implements AsyncCallback<ArrayList<Broker>>{

		/**
		 * METHOD:	ON SUCCESS
		 * Populate brokerListWidget with broker data		 */
		public void onSuccess(ArrayList<Broker> bList){

			//set list for later use
			setBrokerList(bList);

			//iterate and add to list
			for(Broker b: brokerList){
				brokerListBox.addItem(b.getFullname());
			}
		}

		/**
		 * METHOD:	ON FAILURE
		 * Throw exception		 */
		public void onFailure(Throwable ex){
			Window.alert("RPC Failure");			
		}
	}
}