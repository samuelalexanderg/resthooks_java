package com.softwareag.mediator.server.store;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StartResponseType {
	private int status;
	private String checksum;
	private StartTripType startTrip;

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getChecksum() {
		return this.checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public StartTripType getStartTrip() {
		return this.startTrip;
	}

	public void setStartTrip(StartTripType startTrip) {
		this.startTrip = startTrip;
	}
}
