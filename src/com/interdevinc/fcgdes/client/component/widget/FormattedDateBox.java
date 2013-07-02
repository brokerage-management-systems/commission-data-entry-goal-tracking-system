package com.interdevinc.fcgdes.client.component.widget;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class FormattedDateBox extends DateBox{

    private final DateTimeFormat dateFieldFormat=DateTimeFormat.getFormat("MMM dd yyyy");
    private final DateTimeFormat databaseTimeFormat=DateTimeFormat.getFormat("yyyyMMdd");

    public FormattedDateBox(){

	//set modified date format
	setFormat(new DateBox.DefaultFormat(dateFieldFormat));
    }

    /**
     * METHOD:	GET TEXT VALUE FROM DATEBOX
     * @return - String date value	 */
    public String getText(){
	return databaseTimeFormat.format(getValue());
    }
}