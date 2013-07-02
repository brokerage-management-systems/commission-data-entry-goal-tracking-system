package com.interdevinc.fcgdes.client.component;

import com.interdevinc.fcgdes.client.FCG;

public interface Containable {

	/**
	 * METHOD:	SET CONTAINER
	 * Sets container for external manipulation	 */
	public void setContainer(ManagerView view);

	/**
	 * METHOD:	SET MAIN MODULE
	 * @param main	 */
	public void setMainModule(FCG main);
}
