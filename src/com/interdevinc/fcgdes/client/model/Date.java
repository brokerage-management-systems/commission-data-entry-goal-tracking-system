package com.interdevinc.fcgdes.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

@SuppressWarnings("unused")
public class Date implements IsSerializable {

    private String commissionMonthStart;
    private String commissionMonthEnd;
    private String previousMonth;
    private String nextMonth;
    private String monthLabel;
    private String commissionTodayDate;

    public Date() {
    }

    public String getCommissionMonthEnd() {
      return commissionMonthEnd;
    }

    public String getCommissionMonthStart() {
      return commissionMonthStart;
    }

    public String getMonthLabel() {
      return monthLabel;
    }

    public String getNextMonth() {
      return nextMonth;
    }

    public String getPreviousMonth() {
      return previousMonth;
    }

    public void setCommissionMonthEnd(String cme) {
      commissionMonthEnd = cme;
    }

    public void setCommissionMonthStart(String cms) {
      commissionMonthStart = cms;
    }

    public void setMonthLabel(String ml) {
      monthLabel = ml;
    }

    public void setNextMonth(String nm) {
      nextMonth = nm;
    }

    public void setPreviousMonth(String pm) {
      previousMonth = pm;
    }

}
