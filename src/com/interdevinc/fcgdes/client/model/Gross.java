package com.interdevinc.fcgdes.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Gross implements IsSerializable {

	private String grossID;
	private String repNumber;
	private String grossType;
	private String grossDate;
	private String grossAmt;
	private String newAcct;
	private String reneg;

	private String dealGross;
	private String insuranceGross;
	private String legentGross;
	private String moneyRaised;
	private String nfsGross;
	private String totalGross;

	// For Gross Report 
	private String commissionDayDate;
	private String commissionBeginDate;
	private String commissionEndDate;

	private String totalDealGross;
	private String totalInsuranceGross;
	private String totalLegentGross;
	private String totalMoneyRaisedGross;
	private String totalNFSGross;
	private String totalNewAccount;
	private String totalReneg;

	private String repName;

	// For Goal Charts
	private String[] currentYearTotalGross;
	private String[] currentYearTotalMoneyRaised;
	private String[] currentTotalNewAccounts;

	private String[] previousYearTotalGross;
	private String[] previousYearTotalMoneyRaised;
	private String[] previousTotalNewAccounts;

	public Gross(){

	}

	public Gross(String gd, String dl, String ig, String lg, String mr, String na, String ng, String re){
		setGrossDate(gd);
		setDealGross(dl);
		setInsuranceGross(ig);
		setLegentGross(lg);
		setMoneyRaised(mr);
		setNewAccount(na);
		setNFSGross(ng);
		setReneg(re);
	}

	public Gross(String id, String gd, String dl, String ig, String lg, String mr, String na, String ng, String re){
		setGrossID(id);
		setGrossDate(gd);
		setDealGross(dl);
		setInsuranceGross(ig);
		setLegentGross(lg);
		setMoneyRaised(mr);
		setNewAccount(na);
		setNFSGross(ng);
		setReneg(re);
	}

	public Gross(String gi, String rn, String gd, String dl, String ig, String lg, String mr, String na, String ng, String re){
		setGrossID(gi);
		setRepNumber(rn);
		setGrossDate(gd);
		setDealGross(dl);
		setInsuranceGross(ig);
		setLegentGross(lg);
		setMoneyRaised(mr);
		setNewAccount(na);
		setNFSGross(ng);
		setReneg(re);
	}

	// Get Gross Methods
	public String getCommissionBeginDate(){
		return commissionBeginDate;
	}

	public String getCommissionDayDate(){
		return commissionDayDate;
	}

	public String getCommissionEndDate(){
		return commissionEndDate;
	}

	/**
	 * @return the currentYearTotalGross
	 */
	public String[] getCurrentYearTotalGross() {
		return currentYearTotalGross;
	}

	/**
	 * @return the currentYearTotalMoneyRaised
	 */
	public String[] getCurrentYearTotalMoneyRaised() {
		return currentYearTotalMoneyRaised;
	}

	/**
	 * @return the currentTotalNewAccounts
	 */
	public String[] getCurrentTotalNewAccounts() {
		return currentTotalNewAccounts;
	}

	public String getGrossAmount(){
		return grossAmt;
	}

	public String getGrossDate(){
		return grossDate;
	}

	public String getGrossID(){
		return grossID;
	}

	public String getGrossType(){
		return grossType;
	}

	public String getDealGross(){
		return dealGross;
	}

	public String getInsuranceGross(){
		return insuranceGross;
	}

	public String getLegentGross(){
		return legentGross;
	}

	public String getMoneyRaised(){
		return moneyRaised;
	}

	public String getNewAccount(){
		return newAcct;
	}

	public String getNFSGross(){
		return nfsGross;
	}

	/**
	 * @return the previousYearTotalGross
	 */
	public String[] getPreviousYearTotalGross() {
		return previousYearTotalGross;
	}

	/**
	 * @return the previousYearTotalMoneyRaised
	 */
	public String[] getPreviousYearTotalMoneyRaised() {
		return previousYearTotalMoneyRaised;
	}

	/**
	 * @return the previousTotalNewAccounts
	 */
	public String[] getPreviousTotalNewAccounts() {
		return previousTotalNewAccounts;
	}

	public String getReneg(){
		return reneg;
	}

	public String getRepName(){
		return repName;
	}

	public String getRepNumber(){
		return repNumber;
	}

	public String getTotalGross(){
		return totalGross;
	}

	// Set Gross Methods
	public void setCommissionBeginDate(String cbd){
		commissionBeginDate=cbd;
	}

	public void setCommissionDayDate(String cdd){
		commissionDayDate=cdd;
	}

	public void setCommissionEndDate(String ced){
		commissionEndDate=ced;
	}

	/**
	 * @param cytg the currentYearTotalGross to set
	 */
	public void setCurrentYearTotalGross(String[] cytg) {
		currentYearTotalGross = cytg;
	}

	/**
	 * @param cytmr the currentYearTotalMoneyRaised to set
	 */
	public void setCurrentYearTotalMoneyRaised(String[] cytmr) {
		currentYearTotalMoneyRaised = cytmr;
	}

	/**
	 * @param ctna the currentTotalNewAccounts to set
	 */
	public void setCurrentTotalNewAccounts(String[] ctna) {
		currentTotalNewAccounts = ctna;
	}

	public void setGrossAmount(String ga){
		grossAmt=ga;
	}

	public void setGrossDate(String gd){
		grossDate=gd;
	}

	public void setGrossID(String gi){
		grossID=gi;
	}

	public void setGrossType(String gt){
		grossType=gt;
	}

	public void setDealGross(String dg){
		dealGross=dg;
	}

	public void setInsuranceGross(String ig){
		insuranceGross=ig;
	}

	public void setLegentGross(String lg){
		legentGross=lg;
	}

	public void setMoneyRaised(String mr){
		moneyRaised=mr;
	}

	public void setNewAccount(String na){
		newAcct=na;
	}

	public void setNFSGross(String ng){
		nfsGross=ng;
	}

	/**
	 * @param pytg the previousYearTotalGross to set
	 */
	public void setPreviousYearTotalGross(String[] pytg) {
		previousYearTotalGross = pytg;
	}

	/**
	 * @param pytmr the previousYearTotalMoneyRaised to set
	 */
	public void setPreviousYearTotalMoneyRaised(String[] pytmr) {
		previousYearTotalMoneyRaised = pytmr;
	}

	/**
	 * @param ptna the previousTotalNewAccounts to set
	 */
	public void setPreviousTotalNewAccounts(String[] ptna) {
		previousTotalNewAccounts = ptna;
	}

	public void setReneg(String re){
		reneg=re;
	}

	public void setRepName(String rn){
		repName=rn;
	}

	public void setRepNumber(String rn){
		repNumber=rn;
	}

	public void setTotalGross(String tg){
		totalGross=tg;
	}

	public void setTotalDealGross(String tdg){
		totalDealGross=tdg;
	}

	public String getTotalDealGross(){
		return totalDealGross;
	}

	public void setTotalInsuranceGross(String tig){
		totalInsuranceGross=tig;
	}

	public String getTotalInsuranceGross(){
		return totalInsuranceGross;
	}

	public void setTotalLegentGross(String tlg){
		totalLegentGross=tlg;
	}

	public String getTotalLegentGross(){
		return totalLegentGross;
	}

	public void setTotalMoneyRaisedGross(String tmrg){
		totalMoneyRaisedGross=tmrg;
	}

	public String getTotalMoneyRaisedGross(){
		return totalMoneyRaisedGross;
	}

	public void setTotalNewAcct(String tna){
		totalNewAccount=tna;
	}

	public String getTotalNewAcct(){
		return totalNewAccount;
	}

	public void setTotalNfsGross(String tng){
		totalNFSGross=tng;
	}

	public String getTotalNfsGross(){
		return totalNFSGross;
	}

	public void setTotalReneg(String tre){
		totalReneg=tre;
	}

	public String getTotalReneg(){
		return totalReneg;
	}

	public String toHTMLString(){
		StringBuilder builder=new StringBuilder();

		builder.append("Date: "+getGrossDate()+"<br/>");
		builder.append("NFS: "+getNFSGross()+"<br/>");
		builder.append("Legent: "+getLegentGross()+"<br/>");
		builder.append("Deal: "+getDealGross()+"<br/>");
		builder.append("Insurance: "+getInsuranceGross()+"<br/>");
		builder.append("Money Raised: "+getMoneyRaised()+"<br/>");
		builder.append("New Account(s): "+getNewAccount()+"<br/>");
		builder.append("Reneg(s): "+getReneg()+"<br/>");

		return builder.toString();
	}

	public String toString(){
		StringBuilder builder=new StringBuilder();

		builder.append("Date: "+getGrossDate()+"\n");
		builder.append("New Account(s): "+getNewAccount()+"\n");
		builder.append("Legent: "+getLegentGross()+"\n");
		builder.append("Deal: "+getDealGross()+"\n");
		builder.append("Insurance: "+getInsuranceGross()+"\n");
		builder.append("NFS: "+getNFSGross()+"\n");
		builder.append("Reneg(s): "+getReneg()+"\n");

		return builder.toString();
	}
}