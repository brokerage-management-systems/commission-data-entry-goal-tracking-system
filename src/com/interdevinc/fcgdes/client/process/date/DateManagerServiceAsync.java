package com.interdevinc.fcgdes.client.process.date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.Date;

public interface DateManagerServiceAsync {

    public void retrieveCurrentMonthDates(String date, AsyncCallback<Date> callback);

}
