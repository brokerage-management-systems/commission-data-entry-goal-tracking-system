package com.interdevinc.fcgdes.client.process.report;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;

@RemoteServiceRelativePath("reportManager")
public interface ReportManagerService extends RemoteService {

    public ArrayList<Gross> generateGrossReportData(Gross g);

    public Broker generateGoalReportData(Goal g);


}
