package com.softwareag.mediator.server;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.softwareag.mediator.server.domain.Customer;

/**
* @author mgunasek
*/
@XmlType(name = "")
@XmlRootElement(name = "customers")
public class CustomersHolder {

    @XmlElement(name = "customer")
    List<Customer> customers;

    public CustomersHolder() {
        customers = new ArrayList<Customer>();
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
