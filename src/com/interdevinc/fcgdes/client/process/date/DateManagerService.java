package com.interdevinc.fcgdes.client.process.date;

import com.google.gwt.user.client.rpc.RemoteService;

import com.interdevinc.fcgdes.client.model.Date;

public interface DateManagerService extends RemoteService {

    public Date retrieveCurrentMonthDates(String date);


}
