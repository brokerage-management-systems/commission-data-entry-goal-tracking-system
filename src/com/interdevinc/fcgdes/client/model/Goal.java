package com.interdevinc.fcgdes.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Goal implements IsSerializable {

    private String goalYear;
    private String moneyRaised;
    private String newAccounts;
    private String repNumber;
    private String totalGross;

    private String[] goalTotalGross;
    private String[] goalTotalMoneyRaised;
    private String[] goalTotalNewAccounts;

    public Goal() {
    }

    public Goal(String rn, String gy, String mr, String na, String tg){
      setRepNumber(rn);
      setGoalYear(gy);
      setMoneyRaised(mr);
      setNewAccounts(na);
      setTotalGross(tg);
    }

    /**
     * @return the goalTotalGross	 */
    public String[] getGoalTotalGross() {
      return goalTotalGross;
    }

    /**
     * @return the goalTotalMoneyRaised	 */
    public String[] getGoalTotalMoneyRaised() {
      return goalTotalMoneyRaised;
    }

    /**
     * @return the goalTotalNewAccounts	 */
    public String[] getGoalTotalNewAccounts() {
      return goalTotalNewAccounts;
    }

    public String getGoalYear() {
      return goalYear;
    }

    public String getMoneyRaised() {
      return moneyRaised;
    }

    public String getNewAccounts() {
      return newAccounts;
    }

    public String getRepNumber() {
      return repNumber;
    }

    public String getTotalGross() {
      return totalGross;
    }

    /**
     * @param gtg the goalTotalGross to set
     */
    public void setGoalTotalGross(String[] gtg) {
      goalTotalGross = gtg;
    }

    /**
     * @param gtmr the goalTotalMoneyRaised to set
     */
    public void setGoalTotalMoneyRaised(String[] gtmr) {
      goalTotalMoneyRaised = gtmr;
    }

    /**
     * @param gtna the goalTotalNewAccounts to set
     */
    public void setGoalTotalNewAccounts(String[] gtna) {
      goalTotalNewAccounts = gtna;
    }

    public void setGoalYear(String gy) {
      goalYear = gy;
    }

    public void setMoneyRaised(String mr) {
      moneyRaised = mr;
    }

    public void setNewAccounts(String na) {
      newAccounts = na;
    }

    public void setRepNumber(String rn) {
      repNumber = rn;
    }

    public void setTotalGross(String tg) {
      totalGross = tg;
    }

}
