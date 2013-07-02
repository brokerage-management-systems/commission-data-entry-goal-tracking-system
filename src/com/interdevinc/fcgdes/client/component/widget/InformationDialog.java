package com.interdevinc.fcgdes.client.component.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

public class InformationDialog  extends DialogBox {

	public InformationDialog() {

	}

	public void loadBugSubmissionForm() {

		//HTML content = new HTML("<iframe src='https://spreadsheets.google.com/embeddedform?formkey=dG1QQmpRTXByMTcwRFJEWXk1QVVxLWc6MA' width='775' height='500' frameborder='0' marginheight='0' marginwidth='0'>Loading...</iframe>");
		HTML content = new HTML("<iframe src='http://mantis.interdevinc.com/bug_report_page.php' width='775' height='500' frameborder='0' marginheight='0' marginwidth='0'>Loading...</iframe>");
		final Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		Grid grid = new Grid(2, 1);
		grid.setWidget(0, 0, content);
		grid.setWidget(1, 0, cancelButton);

		setText("Bug Submission");
		add(grid);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		center();

	}

	public void loadComponentLoadingDialog() {
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setText("Component Loading...");
		HTML content = new HTML("<div align='center'><img align='center' src='images/loading.gif' border=0 /></div><p><b>Please be patient while the application<br />loads the necessary components.</b></p>");
		add(content);
		center();
	}

	public void loadReportLoadingDialog() {
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setText("Your Report is being Generated...");
		HTML content = new HTML("<div align='center'><img align='center' src='images/loading.gif' border=0 /></div><p><b>Please be patient while the Report<br />you selected is Generated.</b></p>");
		add(content);
		center();
	}

	@SuppressWarnings("unused")
	public void loadSystemStatusDialog() {
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setText("System Status.");
		setAutoHideEnabled(true);
		String logoImageLink = "<a href='http://www.interdevinc.com/'><img align='center' width='150' height='78' src='images/gappidevlogo.gif' border=0 /></a><br />";
		String siteStatus = "";
		String databaseStatus = "";
		HTML content = new HTML(logoImageLink);
		add(content);
		center();
	}

	public void loadVersionDialog() {
		setGlassEnabled(true);
		setAnimationEnabled(true);
		setText("Version Information.");
		setAutoHideEnabled(true);
		HTML content = new HTML("<a href='http://www.interdevinc.com/'><img align='center' width='150' height='78' src='images/gappidevlogo.gif' border=0 /></a><br />InterDev Inc. &copy; Copyright 2010<br /><a href='http://www.interdevinc.com'>http://www.interdevinc.com</a><br /><a href=mailto:'support@interdevinc.com'>support@interdevinc.com</a><br />Commission Data Entry System<br />Version 2.01<br />");
		add(content);
		center();
	}

}
