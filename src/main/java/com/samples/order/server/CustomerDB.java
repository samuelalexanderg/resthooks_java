package com.softwareag.mediator.server;

import com.softwareag.mediator.server.domain.Customer;
import com.softwareag.mediator.server.domain.Order;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In-memory db for the application
 *
 * @author mgunasek
 */
public class CustomerDB {

    private String customersXmlFilePath = "/customers.xml";

    private JAXBContext ctx;

    private Unmarshaller unmarshaller;
    private Marshaller marshaller;

    private List<Customer> customers;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private static final CustomerDB instance = new CustomerDB();

    public static CustomerDB get() {
        return instance;
    }


    protected CustomerDB() {
        initJaxb();
        this.customers = getCustomersFromFile();
        logger.fine("Initialized app db...");
    }


    public List<Customer> getCustomers() {
        if (this.customers == null) {
            this.customers = Collections.emptyList();
        }
        return this.customers;
    }

    public Customer getCustomer(String id) {
        Customer customer = null;
        for (Customer a: getCustomers()) {
            if (a.getId().equals(id)) {
                customer = a;
                break;
            }
        }
        return customer;
    }
    
    public Customer getCustomerByName(String name) {
        Customer customer = null;
        for (Customer a: getCustomers()) {
            if (a.getCompanyName().equalsIgnoreCase(name)) {
                customer = a;
                break;
            }
        } 
        return customer;
    }

    public Customer getCustomerByPhone(String phone) {
        Customer customer = null;
        for (Customer a: getCustomers()) {
            if (a.getPhone().equalsIgnoreCase(phone)) {
                customer = a;
                break;
            }
        } 
        return customer;
    }

    public boolean createCustomer(Customer customer) {
        this.customers.add(customer);
        return storeCustomers();
    }

	public List<Order> getAllOrders() {
		List<Order> orders = new ArrayList<Order>();
		for (Customer customer : getCustomers()) {
			orders.addAll(customer.getOrders());
		}
		
		return orders;
	}
	
    protected void initJaxb() {
        try {
            ctx = JAXBContext.newInstance(CustomersHolder.class);
            unmarshaller = ctx.createUnmarshaller();
            marshaller = ctx.createMarshaller();

        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "JAXB error", e);
        }
    }


    protected boolean storeCustomers() {
        boolean isStored = false;
        CustomersHolder holder = new CustomersHolder();
        holder.customers = customers;
        
        URL url = this.getClass().getResource(customersXmlFilePath);
        if (url!=null) {
            try {
                File file = new File(url.toURI());
                if (file.exists()) {
                    marshaller.marshal(holder, file);

                    isStored = true;
                }
                else {
                    logger.warning("Could not locate file: " + customersXmlFilePath);
                }
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "", e);
            } catch (JAXBException e) {
                logger.log(Level.SEVERE, "JAXB error", e);
            }
        }
        else {
            logger.warning("Could not get resource: " + customersXmlFilePath);
        }

        return isStored;
    }

    protected List<Customer> getCustomersFromFile() {
        List<Customer> customerList = null;
        InputStream is = this.getClass().getResourceAsStream(customersXmlFilePath);
        if (is != null) {
            try {
                Object o = unmarshaller.unmarshal(is);
                if (o != null && o instanceof CustomersHolder) {
                    customerList = ((CustomersHolder) o).customers;
                    if (customerList!=null) {
                        logger.info("Loaded " + customerList.size() + " customers from file: " + customersXmlFilePath);
                    }
                    else {
                        logger.warning("authors info is null!");
                    }

                } else {
                    logger.warning("Could not load customers information!");
                }

            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
        return customerList;
    }

}


