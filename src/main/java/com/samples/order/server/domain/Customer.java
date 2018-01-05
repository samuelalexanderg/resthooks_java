package com.softwareag.mediator.server.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author mgunasek
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "CustomerType",
         propOrder = {"companyName", "contactName", "phone", "address", "orders" })
public class Customer {

    @XmlID
    @XmlAttribute
    private String id;  //should be string type for @XmlID

    @XmlElement(required = true)
    private String companyName;
    
    @XmlElement(required = true)
    private String contactName;
    
    @XmlElement
    private String phone;

    @XmlElement(required = true)
    private Address address;

    @XmlElementWrapper(name = "orders")
    @XmlElements(@XmlElement(name = "order", type = Order.class))
    private List<Order> orders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        if (orders == null) {
        	orders = new ArrayList<Order>();
        }
        return orders;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
