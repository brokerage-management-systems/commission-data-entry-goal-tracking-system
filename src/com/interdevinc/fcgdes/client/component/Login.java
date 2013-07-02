package com.interdevinc.fcgdes.client.component;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.interdevinc.fcgdes.client.FCG;
import com.interdevinc.fcgdes.client.model.AuthenticatedUser;
import com.interdevinc.fcgdes.client.process.Hash;
import com.interdevinc.fcgdes.client.process.user.AuthenticateUser;
import com.interdevinc.fcgdes.client.process.user.AuthenticateUserAsync;

@SuppressWarnings("unused")
public class Login extends Composite implements ClickHandler,KeyPressHandler{

	private FlexTable table;
	private Image userImg;
	private Image passImg;
	private TextBox nameBox;
	private PasswordTextBox passBox;

	private Label errorLabel;

	public Button submit;

	private FCG main;

	//RPC objects
	private AuthenticateUserAsync authentication;
	private AuthenticationHandler authenticationHandler;

	public Login(FCG f){

		main=f;

		//init components
		initAllComponents();

		//init RPC objects
		initRemoteProcedureWorkers();

		//build flex table
		assembleComponents();

		//initialize composite as flextable
		initWidget(table);

		//set style
		setStylePrimaryName("login-form-container");
	}

	/**
	 * METHOD:	ON CLICK
	 * Click events for associating buttons	 */
	public void onClick(ClickEvent ce){
		//clear form
		errorLabel.setText("");

		if(validFormFields()){

			//get fields and hash password
			final String username=nameBox.getText();
			final String password=Hash.md5(passBox.getText());

			//execute authentication procedure
			authentication.authenticateUser(username,password,authenticationHandler);
		}else{
			//display error
			errorLabel.setText("Please fill all form fields!");
		}
	}

	/**
	 * METHOD:	ON KEY PRESS
	 * Keyboard events for associated widgets/components	 */
	public void onKeyPress(KeyPressEvent keyEvent) {
		if(keyEvent.getNativeEvent().getKeyCode()==KeyCodes.KEY_ENTER){
			submit.click();
		}
	}

	/**
	 * METHOD:	ASSEMBLE COMPONENTS
	 * This method assembles all the components for 
	 * the login component	 */
	private void assembleComponents(){
		//set row 0
		table.setWidget(0, 0, userImg);
		table.setWidget(0,1,nameBox);

		//set row 1
		table.setWidget(1, 0, passImg);
		table.setWidget(1,1,passBox);

		//set row 2
		table.setWidget(2,0,submit);
		table.getFlexCellFormatter().setColSpan(2,0,2);
		table.getFlexCellFormatter().setStylePrimaryName(2,0,"form-button-row");

		table.setWidget(3,0,errorLabel);
		table.getFlexCellFormatter().setColSpan(3,0,2);
		table.getFlexCellFormatter().setStylePrimaryName(3,0,"error-label");
	}

	/**
	 * METHOD:	CHECK VALID FORM FIELDS
	 * This method checks wether both username and
	 * password fields have been filled properly
	 * @return - true/false	 */
	private boolean validFormFields(){
		return (!nameBox.getText().equals("") && !passBox.getText().equals(""));
	}

	/**
	 * METHOD:	INIT LOGIN COMPONENTS
	 * This method initializes all components 
	 * associated with the login widget: username,
	 * password, and submit field	 */
	private void initAllComponents(){

		// Images for the username/password icons.
		userImg = new Image();
		userImg.setUrl("/images/user.gif");
		userImg.setSize("32px", "32px");
		passImg = new Image();
		passImg.setUrl("/images/pass.gif");
		passImg.setSize("32px", "32px");

		//init table
		table=new FlexTable();

		//init data fields
		nameBox=new TextBox();
		nameBox.setWidth("100px");		
		nameBox.addKeyPressHandler(this);
		//nameBox.setText("testuser");

		passBox=new PasswordTextBox();		
		passBox.setWidth("100px");
		passBox.addKeyPressHandler(this);
		//passBox.setText("m0Nt3F0rt3");

		//init submit button
		submit=new Button("Login");
		submit.addClickHandler(this);

		//init default error label
		errorLabel=new Label("");
	}

	/**
	 * METHOD:	INIT REMOTE PROCEDURE WORKERS
	 * This method initializes all object associated with
	 * creating a valid RPC call.	 */
	private void initRemoteProcedureWorkers(){
		//define the service to call  
		authentication=(AuthenticateUserAsync)GWT.create(AuthenticateUser.class);    

		//init RPC handler
		authenticationHandler=new AuthenticationHandler();
	}

	/**
	 * PRIVATE CLASS: AUTHENTICATION HANDLER
	 * @author floresj4
	 * This class handles the Remote Procedure Calls associated with
	 * AuthenticateUserImp.  Because a successful RPC call may still constitute and
	 * NULL user, onSuccess(Auth...) will test for an unfound user as NULL and display
	 * the appropriate error message  	 */
	private class AuthenticationHandler implements AsyncCallback<AuthenticatedUser>{

		/**
		 * METHOD:	ON SUCCESS
		 * @param user - valid user or NULL		 */
		public void onSuccess(AuthenticatedUser user){
			if(user!=null){
				//set the user as authenticated for login/out purposes
				main.setAuthenticatedUser(user);
				//create history token
				History.newItem("managerView");
			}else{
				errorLabel.setText("Username/Password pair not found.");
			}				
		}

		/**
		 * METHOD:	ON FAILURE
		 * @param ex - exception throw on RPC failure */
		public void onFailure(Throwable ex){
			Window.alert("The JVM appears to be down.\nPlease email support@interdevinc.com and inform them the the JVM is not functioning.\nAdd the following to your email message:\n" + ex.getMessage());
		}
	}
}