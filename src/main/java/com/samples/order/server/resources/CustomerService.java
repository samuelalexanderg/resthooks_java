package com.softwareag.mediator.server.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.softwareag.mediator.server.CustomerDB;
import com.softwareag.mediator.server.domain.Customer;
import com.softwareag.mediator.server.domain.Order;
import com.sun.jersey.spi.resource.Singleton;

/**
 * This is the AuthorService that exposes a bunch of REST endpoints each of which can handle a
 * certain HTTP method and/or specific content-type
 * 
 * @author mgunasek
 */

@Singleton
@Path("customer")
public class CustomerService {

	private CustomerDB db;
	private static final Logger logger = Logger.getLogger(CustomerService.class.getPackage().getName());

	private AtomicInteger authorId = new AtomicInteger(4); // start at 4, because, we already have

	// 3 preset authors

	public CustomerService() {
		this.db = CustomerDB.get();
	}

	private int getNextId() {
		return authorId.getAndIncrement();
	}

	@GET
	@Produces({"application/xml", "application/json"})
	public List<Customer> getCustomers() {
		logger.fine("Returning all customers...");
		return db.getCustomers();
	}

	/**
	 * Get author info by id
	 * 
	 * @param id
	 */
	@GET
	@Produces({"application/xml", "application/json"})
	@Path("{id}")
	public Customer getCustomerByID(@PathParam("id")
	String id) {
		logger.fine("looking up author for id: " + id);
		Customer customer = db.getCustomer(id);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return customer;
	}

	@GET
	@Path("order")
	@Produces({"application/xml", "application/json"})
	public List<Order> getAllOrders() {
		logger.fine("Returning all authors...");
		return db.getAllOrders();
	}

	/**
	 * Get author info by id
	 * 
	 * @param id
	 */
	@GET
	@Produces({"application/xml", "application/json"})
	@Path("{custId}/order")
	public List<Order> getAllOrders(@PathParam("custId")
	String id) {
		logger.fine("looking up customer for id: " + id);
		Customer customer = db.getCustomer(id);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return customer.getOrders();
	}

	/**
	 * Get customer by first or last name
	 * 
	 * @param name -
	 *            either first or last name
	 * @return
	 */
	@GET
	@Path("{custId}/order/{orderId}")
	@Produces({"application/xml", "application/json"})
	public Order getOrder(@PathParam("custId") String custId, @PathParam("orderId") String orderId) {
		logger.fine("Looking up customer using id : " + custId);
		Customer customer = db.getCustomer(custId);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		List<Order> orders = customer.getOrders();
		for (Order order : orders) {
			if (order.getId().equals(orderId)) {
				return order;
			}
		}
		
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	/**
	 * Get customer info using specified query params in the request uri
	 * 
	 * @param uri
	 * @return
	 */
	@GET
	@Produces({"application/xml", "application/json"})
	@Path("query")
	public Customer getCustomersByQueryParams(@Context
	UriInfo uri) {
		Customer customer = null;

		MultivaluedMap<String, String> queryParams = uri.getQueryParameters();
		if (queryParams != null) {
			List<String> phones = queryParams.get("phone");
			List<String> customerNames = queryParams.get("customerName");

			if (phones != null && customerNames != null) {

				logger.finest("Received " + phones.size() + " 'firstName' query params");
				logger.finest("Received " + customerNames.size() + " 'lastName' query params");

				String phone = phones.get(0);
				String customerName = customerNames.get(0);

				logger.fine("Looking up author using query params; first name: " + phone + " and last name: " + customerName);

				customer = db.getCustomerByName(customerName);
				if (customer == null || !customer.getPhone().equals(phone)) {
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
			} else if (phones != null || customerNames != null) {

				if (phones != null)
					logger.finest("Received " + phones.size() + " 'Phone' query params");
				else if (customerNames != null)
					logger.finest("Received " + customerNames.size() + " 'customerName' query params");

				String phone = null;
				String customerName = null;
				if (phones != null) {
					phone = phones.get(0);
					customer = db.getCustomerByPhone(phone);
				} else if (customerNames != null) {
					customerName = customerNames.get(0);
					customer = db.getCustomerByName(customerName);
				}

				if (customer == null) {
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
			} else {
				logger.warning("Either phone and/or customerName query params are missing in request!");
			}
		} else {
			logger.warning("Query params were not specified! Cannot lookup author info!");
		}

		return customer;
	}

	/**
	 * Create a new author based on the incoming &lt;author/&gt; xml
	 * 
	 * @param author
	 * @return
	 */
	@POST
	@Consumes({"application/xml", "application/json"})
	public Response createCustomer(Customer customer, @Context UriInfo uriInfo) {
		int id = getNextId();
		customer.setId("ID" + String.valueOf(id));
		logger.fine("Creating new customer with id: " + id);

		boolean isCreated = db.createCustomer(customer);
		if (!isCreated) {
			logger.fine("Could not store customer");
			throw new WebApplicationException();
		} else {
			URI uri = uriInfo.getBaseUriBuilder().path("customer/{id}").build(customer.getId());
			logger.fine("Created author with id: " + customer.getId() + " with path: " + uri);
			return Response.created(uri).build();
		}
	}

	@PUT
	@Consumes({"application/xml", "application/json"})
	public Response updateCustomer(Customer customer, @PathParam("id") String id) throws URISyntaxException {
		if (id == null) {
			id = customer.getId();
		}
		boolean isUpdated = false;
		Customer oldCustomer = db.getCustomer(id);
		if (oldCustomer != null) {
			oldCustomer.setAddress(customer.getAddress());
			oldCustomer.setCompanyName(customer.getCompanyName());
			oldCustomer.setContactName(customer.getContactName());
			oldCustomer.setPhone(customer.getPhone());
			isUpdated = true;
		} else {
			customer.setId("ID" + String.valueOf(id));
			logger.fine("Creating new customer with id: " + id);
			isUpdated = db.createCustomer(customer);
		}
		
		if (!isUpdated) {
			logger.fine("Could not store customer");
			throw new WebApplicationException();
		} else {
			URI uri = new URI("customer/" + customer.getId());
			logger.fine("Created author with id: " + customer.getId() + " with path: " + uri);
			return Response.ok(uri).header("Updated", true).header("CustomerId", id).build();
		}
	}
	
	@PUT
	@Path("{custId}/order")
	@Consumes({"application/xml", "application/json"})
	public Response updateCustomer(@PathParam("custId")
	String id, Order order) {
		Customer customer = db.getCustomer(id);
		if (customer != null) {
			customer.getOrders().add(order);
			return Response.ok().header("Updated", true).header("CustomerId", id).build();
		} else {
			logger.fine("Could not find an existing author with authorId: " + authorId);
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}			 
	}

	@DELETE
	@Path("{id}")
	public Response deleteCustomer(@PathParam("id") String id) {
		Customer customer = db.getCustomer(id);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			logger.fine("Fake Deleting customer with id: " + id);
			return Response.ok().header("Deleted", true).header("CustomerId", id).build();
		}
	}
	
	@DELETE
	@Path("{custId}/order/{orderId}")
	public Response deleteOrder(@PathParam("custId") String custId, @PathParam("orderId") String orderId) {
		boolean orderRemoved = false;
		Customer customer = db.getCustomer(custId);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			List<Order> orders = customer.getOrders();
			if (orders != null) {
				Iterator<Order> iterator = orders.iterator();
				while (iterator.hasNext()) {
					Order order = iterator.next();
					if (order.getId().equals(orderId)) {
						iterator.remove();
						orderRemoved = true;
					}
				}
			}
		}
		if (!orderRemoved) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			return Response.ok().header("Deleted", true).header("CustomerId", custId).header("OrderId", orderId).build();
		}
	}

}
