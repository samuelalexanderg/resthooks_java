package com.softwareag.mediator.server.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author mgunasek
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipInfoType",
         propOrder = {"shipVia", "shipName", "shipAddress", "shippedDate"})
public class ShipInfo {

    @XmlElement(required = true)
    private String shipVia;

    @XmlElement
    private String shipName;

    @XmlElement(required = true)
    private Address shipAddress;

    @XmlElement
	private Date shippedDate;

	public String getShipVia() {
		return shipVia;
	}

	public void setShipVia(String shipVia) {
		this.shipVia = shipVia;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public Address getShipAddress() {
		return shipAddress;
	}

	public void setShipAddress(Address shipAddress) {
		this.shipAddress = shipAddress;
	}

	public Date getShippedDate() {
		return shippedDate;
	}

	public void setShippedDate(Date shippedDate) {
		this.shippedDate = shippedDate;
	}

}
