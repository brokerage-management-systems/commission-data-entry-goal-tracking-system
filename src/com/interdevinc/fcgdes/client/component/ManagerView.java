package com.interdevinc.fcgdes.client.component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.interdevinc.fcgdes.client.FCG;

@SuppressWarnings("unused")
public class ManagerView extends Composite implements ClickHandler {

	private VerticalPanel container;
	private TabPanel tabPanel;
	private Button logout;

	private Manager managers[];

	private FCG mainModule;

	public ManagerView(FCG main){

		//set main module for control
		mainModule=main;

		//init all managers
		initManagersAndAssemble();

		//init composit as vertical panel
		initWidget(container);
	}

	/**
	 * METHOD:	ON CLICK	 */
	public void onClick(ClickEvent ce) {
	}

	/**
	 * METHOD: UPDATE ALL BROKER LISTS Iterates through all managers and updates
	 * each broker list dropdown box	 */
	public void updateAllBrokerLists() {
		//iterate through all managers and update lists
		for (Manager m : managers) {
			m.updateBrokerList();
		}
	}

	/**
	 * METHOD: INIT MANAGERS	 */
	private void initManagersAndAssemble() {

		//init all managing composites
		managers=new Manager[4];
		managers[0]=new BrokerManager("Broker Manager");
		managers[1]=new GoalManager("Goal Manager");
		managers[2]=new GrossManager("Gross Manager");
		managers[3]=new ReportManager("Report Manager");

		//init table panel
		tabPanel=new TabPanel();

		//iterate and add
		for(Manager m:managers) {
			tabPanel.add(m, m.getTitle());
			m.setContainer(this);
			m.updateBrokerList();
			m.setMainModule(mainModule);
		}

		//set first tab as selected
		tabPanel.selectTab(0);

		//init vertical panel
		container=new VerticalPanel();
		container.add(tabPanel);
	}
}