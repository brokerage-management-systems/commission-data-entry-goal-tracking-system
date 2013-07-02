package com.interdevinc.fcgdes.client.process.goal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.interdevinc.fcgdes.client.model.Goal;

@RemoteServiceRelativePath("goalManager")
public interface GoalManagerService extends RemoteService {

    public boolean insertGoal(Goal goal);

    public Goal retrieveBrokerGoal(String repNumber, String goalYear);

    public boolean updateGoal(Goal goal);
}