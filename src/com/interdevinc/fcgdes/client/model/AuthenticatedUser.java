package com.interdevinc.fcgdes.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AuthenticatedUser implements IsSerializable{

  private String userID;
  private String username;
  private String userEmail;

  public AuthenticatedUser(){

  }

  public AuthenticatedUser(String i, String u, String e){
    setUserID(i);
    setUsername(u);
    setUserEmail(e);
  }

  public String getUserEmail(){
    return userEmail;
  }

  public String getUserID(){
    return userID;
  }

  public String getUsername(){
    return username;
  }

  public void setUserEmail(String e){
    userEmail=e;
  }

  public void setUserID(String i){
    userID=i;
  }

  public void setUsername(String u){
    username=u;
  }

  public String toString(){

    StringBuilder builder=new StringBuilder();

    builder.append("id: "+getUserID()+"\n");
    builder.append("username: "+getUsername()+"\n");
    builder.append("email: "+getUserEmail()+"\n");

    return builder.toString();
  }
}
