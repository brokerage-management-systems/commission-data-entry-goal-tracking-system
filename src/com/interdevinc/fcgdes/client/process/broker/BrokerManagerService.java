package com.interdevinc.fcgdes.client.process.broker;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.interdevinc.fcgdes.client.model.Broker;

@RemoteServiceRelativePath("brokerManager")
public interface BrokerManagerService extends RemoteService{

    boolean insertBroker(Broker broker);

    ArrayList<Broker> retrieveBrokerList();

    boolean updateBroker(Broker broker);
}
