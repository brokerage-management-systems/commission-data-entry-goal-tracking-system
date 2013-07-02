package com.interdevinc.fcgdes.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.interdevinc.fcgdes.client.component.Login;
import com.interdevinc.fcgdes.client.component.ManagerView;
import com.interdevinc.fcgdes.client.component.ReportView;
import com.interdevinc.fcgdes.client.component.widget.InformationDialog;
import com.interdevinc.fcgdes.client.model.AuthenticatedUser;
import com.interdevinc.fcgdes.client.model.Goal;
import com.interdevinc.fcgdes.client.model.Gross;

@SuppressWarnings("unused")
public class FCG implements EntryPoint, ClickHandler, CloseHandler<Window>, 
	ValueChangeHandler<String>, Window.ClosingHandler
{
	private Label bugSubmissionLabel;
	private Label logoutLabel;
	private Label systemStatusLabel;
	private Label versionLabel;
	private Login loginScreen;
	private ManagerView managerview;

	private AuthenticatedUser authenticatedUser;

	private final Date date = new Date();
	private final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyyMMdd HH:mm:ss");

	public void onModuleLoad()
	{
		//Wire browser history
		initHistoryState();
		Window.addCloseHandler(this);
		//loadPageFooter();
	}

	/**
	 * METHOD:	ON CLICK
	 */
	public void onClick(ClickEvent ce)
	{
		Widget sender = (Widget)ce.getSource();

		if (sender.equals(bugSubmissionLabel)) {
			InformationDialog dialog = new InformationDialog();
			dialog.loadBugSubmissionForm();
		}

		if (sender.equals(systemStatusLabel)) {
			InformationDialog dialog = new InformationDialog();
			dialog.loadSystemStatusDialog();
		}

		if (sender.equals(versionLabel)) {
			InformationDialog dialog=new InformationDialog();
			dialog.loadVersionDialog();
		}

		if (sender.equals(logoutLabel)) {
			setAuthenticatedUser(null);
			onHistoryChange("loginView");
		}
	}

	/**
	 * METHOD: ON CLOSE
	 */
	public void onClose(CloseEvent<Window> event)
	{
		// Logoff
		setAuthenticatedUser(null);
		onHistoryChange("loginView");
	}

	/**
	 * METHOD: ON HISTORY CHANGE
	 * @param token - String value of the view.
	 */
	public void onHistoryChange(String token)
	{
		if (token.equals("loginView")) {
			loadLoginView();
		} else if (token.equals("managerView")) {
			loadManagerView();
		}
	}

	/**
	 * METHOD: ON VALUE CHANGE
	 * @param event
	 */
	public void onValueChange(ValueChangeEvent<String> event)
	{
		onHistoryChange(event.getValue());
	}

	@Override
	public void onWindowClosing(ClosingEvent event)
	{

	}

	/**
	 * METHOD:	INIT HISTORY STATE
	 */
	public void initHistoryState()
	{
		//Add the History Handler
		History.addValueChangeHandler(this);

		//Test if any tokens have been passed at startup
		String token=History.getToken();

		if(token.length() == 0){
			onHistoryChange("loginView");
		}else{
			onHistoryChange(token);
		}
	}

	/**
	 * METHOD:	IS AUTHENTICATED USER
	 * @return - returns the evaluation of authUser!=null
	 */
	public boolean isAuthenticatedUser()
	{
		return authenticatedUser != null;
	}

	/**
	 * METHOD:	LOAD PAGE FOOTER
	 */
	public void loadPageFooter()
	{
		bugSubmissionLabel = Label.wrap(Document.get().getElementById(("bug_submission"))); 
		bugSubmissionLabel.addClickHandler(this);
		logoutLabel = Label.wrap(Document.get().getElementById(("logout"))); 
		logoutLabel.addClickHandler(this);
		//	systemStatusLabel = Label.wrap(Document.get().getElementById(("status"))); 
		//	systemStatusLabel.addClickHandler(this);
		versionLabel = Label.wrap(Document.get().getElementById(("version"))); 
		versionLabel.addClickHandler(this);
	}

	/**
	 * METHOD:	LOAD LOGIN VIEW
	 */
	public void loadLoginView()
	{
		//History.newItem("loginView");
		RootPanel.get("main-container").clear();
		loginScreen=new Login(this);
		RootPanel.get("main-container").add(loginScreen);
		loadPageFooter();
	}

	/**
	 * METHOD:	LOAD MANAGER VIEW
	 */
	public void loadManagerView()
	{
		//load if authenticated
		if(isAuthenticatedUser()){
			RootPanel.get("main-container").clear();
			managerview=new ManagerView(this);
			RootPanel.get("main-container").add(managerview);
			loadPageFooter();
		}
	}

	/**
	 * METHOD:	LOAD REPORT VIEW
	 * @param g	- GROSS
	 */
	public void loadReportView(Gross g)
	{
		RootPanel.get("main-container").clear();
		RootPanel.get("main-container").add(new ReportView(g));
		loadPageFooter();
	}

	/**
	 * METHOD:	LOAD REPORT VIEW
	 * @param g - GOAL
	 */
	public void loadReportView(Goal g)
	{
		RootPanel.get("main-container").remove(managerview);
		RootPanel.get("main-container").add(new ReportView(g));
		loadPageFooter();
	}

	/**
	 * METHOD:	SET AUTHENTICATED USER
	 * @param user
	 */
	public void setAuthenticatedUser(AuthenticatedUser user)
	{
		authenticatedUser=user;
	}

}