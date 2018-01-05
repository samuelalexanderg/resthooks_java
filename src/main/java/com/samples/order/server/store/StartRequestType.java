package com.softwareag.mediator.server.store;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StartRequestType {
	private String storeCode;

	public String getStoreCode() {
		return this.storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}
}
