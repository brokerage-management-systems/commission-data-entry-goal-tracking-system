package com.interdevinc.fcgdes.client.process.broker;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.Broker;

public interface BrokerManagerServiceAsync{

    void insertBroker(Broker broker, AsyncCallback<Boolean> async);

    void retrieveBrokerList(AsyncCallback<ArrayList<Broker>> async);

    void updateBroker(Broker broker, AsyncCallback<Boolean> async);
}