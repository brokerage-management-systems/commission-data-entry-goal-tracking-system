package com.interdevinc.fcgdes.client.component;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.interdevinc.fcgdes.client.FCG;
import com.interdevinc.fcgdes.client.component.widget.FormattedDateBox;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;

public class ReportManager extends Manager implements ClickHandler, ChangeHandler {

    private FCG mainModule;

    @SuppressWarnings("unused")
    private ManagerView container;

    private FlexTable componentTable;
    private FlexTable reportTable;

    private Button generateReport;

    private CheckBox selectAllCheckbox;

    private FormattedDateBox commissionDateDatebox;
    private FormattedDateBox commissionBeginDatebox;
    private FormattedDateBox commissionEndDatebox;

    private ListBox reportTypeList;
    private ListBox goalYearList;

    @SuppressWarnings("unused")
    private Broker broker;
    private Goal goal;
    private Gross gross;

    public ReportManager(String t) {
      super(t);

      initAllComponents();
      assembleComponents();
    }

    public void onChange(ChangeEvent event) {

      Widget sender = (Widget)event.getSource();

      if (sender==reportTypeList) {

          // Clear the components if any.
          componentTable.clear();

          // Test which report was choosen and display proper inputs.
          if (reportTypeList.getSelectedIndex()==1) {
        toggleRepSelectionAvailability(true);
        toggleRepSelectionVisibility(true);

        // Add Goal Report Input to the Table.
        componentTable.setWidget(0, 0, new Label("Choose Goal Year"));
        componentTable.setWidget(0, 1, goalYearList);
        componentTable.setWidget(1, 0, generateReport);
        add(componentTable);
          } else if (reportTypeList.getSelectedIndex()==2) {
        selectAllCheckbox.setValue(false);
        toggleRepSelectionAvailability(true);
        toggleRepSelectionVisibility(true);

        // Add Gross Report Inputs to the Table.
        componentTable.setWidget(0, 0, new Label("Check here to select all Brokers."));
        componentTable.setWidget(0, 1, selectAllCheckbox);
        componentTable.setWidget(1, 0, new Label("Commission Date:"));
        componentTable.setWidget(1, 1, commissionDateDatebox);
        componentTable.setWidget(2, 0, new Label("Commission Begin:")); 
        componentTable.setWidget(2, 1, commissionBeginDatebox);
        componentTable.setWidget(3, 0, new Label("Commission End:")); 
        componentTable.setWidget(3, 1, commissionEndDatebox); 
        componentTable.setWidget(4, 0, generateReport);
        add(componentTable);

          }
      }

    }

    public void onClick(ClickEvent event) {

      Widget sender = (Widget)event.getSource();

      if (sender==selectAllCheckbox) {
          if (selectAllCheckbox.getValue()) {
        toggleRepSelectionVisibility(false);
          } else {
        toggleRepSelectionVisibility(true);
          }
      }

      if (sender==generateReport) {
          if (validateRepListData()) {

        String brokersStr;
        if (selectAllCheckbox.getValue()) {
            brokersStr = new String("ALL");
        } else {
            brokersStr = new String(getSelectedRepNumber());
        }

        if (reportTypeList.getSelectedIndex()==1) {
            goal = new Goal();
            goal.setRepNumber(getSelectedRepNumber());
            goal.setGoalYear(goalYearList.getItemText(goalYearList.getSelectedIndex()));
            History.newItem("reportView");
            mainModule.loadReportView(goal);
        }

        if (reportTypeList.getSelectedIndex()==2) {

            gross = new Gross();
            gross.setRepNumber(brokersStr);
            // Check for empty dates.
            if (commissionBeginDatebox.getValue()!=null && commissionDateDatebox.getValue()!=null && commissionEndDatebox.getValue()!=null) {
          gross.setCommissionBeginDate(commissionBeginDatebox.getText());
          gross.setCommissionDayDate(commissionDateDatebox.getText());
          gross.setCommissionEndDate(commissionEndDatebox.getText());

          History.newItem("reportView");
          mainModule.loadReportView(gross);

            } else {
          Window.alert("Please fill in all Date fields!");
            }

        }
          } else {
        Window.alert("Please choose a Broker from the dropdown list, or check the checkbox for all Brokers!");
          }

      }

    }

    /**
     * METHOD: SET CONTAINER Sets container for external manipulation */
    public void setContainer(ManagerView view) {
      container = view;
    }

    public void setMainModule(FCG main){
      mainModule=main;
    }

    private void assembleComponents() {

      toggleRepSelectionAvailability(false);

      reportTable.setWidget(0, 0, new Label("Please choose a Report:"));
      reportTable.setWidget(0, 1, reportTypeList);
      add(reportTable);

    }

    private void initAllComponents() {

      componentTable = new FlexTable();
      reportTable = new FlexTable();

      // DateBoxes for Gross Report Dates
      commissionDateDatebox = new FormattedDateBox();
      commissionBeginDatebox = new FormattedDateBox();
      commissionEndDatebox = new FormattedDateBox();

      // Generate Report Button
      generateReport = new Button("Generate Report");
      generateReport.addClickHandler(this);

      goalYearList = new ListBox();
      goalYearList.addItem("2010");
      goalYearList.addItem("2011");
      goalYearList.addItem("2012");
      goalYearList.addItem("2013");
      goalYearList.addItem("2014");

      reportTypeList = new ListBox();
      reportTypeList.addItem("Select Report Type");
      reportTypeList.addItem("Goal Report");
      reportTypeList.addItem("Gross Report");
      reportTypeList.addChangeHandler(this);

      // Checkbox to select all reps
      selectAllCheckbox = new CheckBox();
      selectAllCheckbox.addClickHandler(this);

    }

    private boolean validateRepListData() {
      boolean repListDataValid = false;

      if (selectAllCheckbox.getValue()) {
          repListDataValid = true;
      } else {

          if (getListBoxIndex() >= 0) {
        repListDataValid = true;
          } else {
        repListDataValid = false;
          }
      }

      return repListDataValid;
    }

}
