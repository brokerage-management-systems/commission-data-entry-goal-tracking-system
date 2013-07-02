package com.interdevinc.fcgdes.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Broker implements IsSerializable {

  private int ID;

  private String repNumber;
  private String firstname;
  private String lastname;
  private String email;

  private boolean active;

  private Goal goalData;

  private Gross grossData;

  public Broker() {
  }

  public Broker(int i, String r, String f, String l, boolean b){
    setID(i);
    setRepNumber(r);
    setFirstname(f);
    setLastname(l);
    setStatus(b);
  }

  public Broker(int i, String r, String f, String l, String e, boolean b){
    setID(i);
    setRepNumber(r);
    setFirstname(f);
    setLastname(l);
    setStatus(b);
    setEmail(e);
  }

  public Goal getGoal() {
    return goalData;
  }

  public Gross getGross() {
    return grossData;
  }

  public boolean getStatus() {
    return active;
  }

  public String getEmail(){
    return email;
  }

  public String getFirstname(){
    return firstname;
  }

  public String getFullname(){
    return lastname+", "+firstname;
  }

  public String getNameFirstLast(){
    return firstname + " " + lastname;
  }

  public int getID(){
    return ID;
  }

  public String getLastname(){
    return lastname;
  }

  public String getRepNumber(){
    return repNumber;
  }

  public void setGoal(Goal g) {
    goalData = g;
  }

  public void setGross(Gross g){
    grossData=g;
  }

  public void setStatus(boolean a){
    active=a;
  }

  public void setEmail(String e){
    email=e;
  }

  public void setFirstname(String f){
    firstname=f;
  }

  public void setID(int i){
    ID=i;
  }

  public void setLastname(String l){
    lastname=l;
  }

  public void setRepNumber(String r){
    repNumber=r;
  }	

  public String toHTMLString(){
    StringBuilder builder=new StringBuilder();

    builder.append("<h4>Broker</h4>");
    builder.append("Rep Number: "+getRepNumber()+"<br/>");
    builder.append("Name: "+getFullname()+"<br/>");
    builder.append("Status: "+(getStatus()?"active":"inactive"));
    builder.append("<h4>Gross Data</h4>");
    builder.append((grossData==null)?"None":grossData.toHTMLString());
    return builder.toString();
  }

  public String toString(){
    StringBuilder builder=new StringBuilder();

    builder.append("Rep Number: "+getRepNumber()+"\n");
    builder.append("Name: "+getFullname()+"\n");
    builder.append("Status: "+(getStatus()?"active":"inactive")+"\n");
    builder.append("Gross Data --------------\n");
    builder.append((grossData==null)?"None":grossData.toString());
    return builder.toString();
  }

}
