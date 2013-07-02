package com.interdevinc.fcgdes.client.process.gross;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Gross;

@RemoteServiceRelativePath("grossManager")
public interface GrossManagerService extends RemoteService {

    public boolean insertGrossData(Broker b);

    public ArrayList<Gross> retrieveGrossDataRange(String repNumber, String commissionDateStart, String commissionDateEnd);

    public boolean updateGrossData(Gross g);
}