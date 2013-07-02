package com.interdevinc.fcgdes.client.component.widget;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

public class Loading extends DialogBox {

    public Loading() {
	setGlassEnabled(true);
	setAnimationEnabled(true);
	setText("Loading...");
	setSize("400px", "300px");
	HTML content = new HTML("<p><b>Please be patient while the application loads the necessary components.</b></p><p><b>...Just remember, JESUS LOVES YOU!!!</b></p>");
	add(content);
	center();
    }

}
