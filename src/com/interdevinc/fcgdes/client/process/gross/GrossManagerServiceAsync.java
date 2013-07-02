package com.interdevinc.fcgdes.client.process.gross;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.Broker;
import com.interdevinc.fcgdes.client.model.Gross;

public interface GrossManagerServiceAsync {

    public void insertGrossData(Broker b, AsyncCallback<Boolean> callback);

    public void retrieveGrossDataRange(String repNumber, String commissionDateStart, String commissionDateEnd, AsyncCallback<ArrayList<Gross>> callback);

    public void updateGrossData(Gross g, AsyncCallback<Boolean> callback);
}