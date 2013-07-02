package com.interdevinc.fcgdes.client.process.report;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;

public interface ReportManagerServiceAsync {

    public void generateGrossReportData(Gross gross, AsyncCallback<ArrayList<Gross>> callback);

    public void generateGoalReportData(Goal goal, AsyncCallback<Broker> callback);


}
