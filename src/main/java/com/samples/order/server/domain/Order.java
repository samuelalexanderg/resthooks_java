package com.softwareag.mediator.server.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author mgunasek
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "OrderType",
         propOrder = {"customerId", "orderDate", "shipInfo"})
public class Order {

    @XmlID
    @XmlAttribute
    private String id;  //should be string type for @XmlID

	@XmlElement(required = true)
    private String customerId;

    @XmlElement
    private String orderDate;

    @XmlElement(required = true)
    private ShipInfo shipInfo;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public ShipInfo getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(ShipInfo shipInfo) {
        this.shipInfo = shipInfo;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
