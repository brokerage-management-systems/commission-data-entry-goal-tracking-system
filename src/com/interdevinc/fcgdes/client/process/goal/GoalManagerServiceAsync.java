package com.interdevinc.fcgdes.client.process.goal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.interdevinc.fcgdes.client.model.Goal;

public interface GoalManagerServiceAsync {

    public void insertGoal(Goal goal, AsyncCallback<Boolean> callback);

    public void retrieveBrokerGoal(String repNumber, String goalYear, AsyncCallback<Goal> callback);

    public void updateGoal(Goal goal, AsyncCallback<Boolean> callback);
}
