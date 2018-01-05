/*
 * Copyright 2012 by Software AG
 *
 * Uhlandstrasse 12, D-64297 Darmstadt, GERMANY
 *
 * All rights reserved
 *
 * This software is the confidential and proprietary
 * information of Software AG ('Confidential Information').
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with Software AG or its distributors.
 */
package com.softwareag.mediator.server.resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.softwareag.mediator.contenttype.Convertor;
import com.softwareag.mediator.server.AppDB;
import com.softwareag.mediator.server.domain.Address;
import com.softwareag.mediator.server.domain.Author;
import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.resource.Singleton;

/**
 * @author MKI This service is for SMGME-1833
 */
@Singleton
@Path("/customTypes")
public class CustomContentTypeRestService {

	private AppDB db;
	private Convertor convertor;
	private static final Logger logger = Logger.getLogger(AuthorService.class
			.getPackage().getName());

	private AtomicInteger authorId = new AtomicInteger(4); // start at 4,
															// because, we
															// already have 3
															// preset authors
	private static final String LAST_ACTION = "Last-Action";

	public CustomContentTypeRestService() {
		this.db = AppDB.get();
		convertor = new Convertor();
	}

	private int getNextId() {
		return authorId.getAndIncrement();
	}

	@GET
	@Path("/getAllAuthorsInXML")
	@Produces({ "application/myXMLContentType" })
	public String getAllAuthorsInXML() {
		logger.fine("Returning all authors...");
		return convertor.convertListToXML(db.getAuthors());
	}

	@GET
	@Produces({ "application/myJsonContentType" })
	@Path("/getAllAuthorsInJson")
	public String getAllAuthorsInJson() {
		logger.fine("Returning all authors...");
		return convertor.convertListToJson(db.getAuthors());
	}

	/**
	 * Create a new author based on the incoming &lt;author/&gt; xml
	 * 
	 * @param author
	 * @return
	 */
	@POST
	@Consumes({ "application/myJsonContentType" })
	@Path("/createAuthorWithJsonRequest")
	public String createAuthorWithJsonRequest(String authorObj,
			@Context UriInfo uriInfo) {

		try {
			Author author = convertor
					.getAuthorInfoFromStringUsingJson(authorObj);
			System.out.println(author.getFirstName());
			int id = getNextId();
			author.setId("ID" + String.valueOf(id));
			logger.fine("Creating new author with id: " + id);

			boolean isCreated = db.createAuthor(author);
			if (!isCreated) {
				logger.fine("Could not store author");
				throw new WebApplicationException();
			} else {
				return "<Result>The author is created and stored with id"
						+ author.getId() + "</Result>";
			}
		} catch (Exception e) {
			throw new WebApplicationException();
		}
	}

	/**
	 * Create a new author based on the incoming &lt;author/&gt; xml
	 * 
	 * @param author
	 * @return
	 */

	@POST
	@Consumes({ "application/myXMLContentType" })
	@Path("/createAuthorWithXMLRequest")
	public String createAuthorWithXMLRequest(String authorObj,
			@Context UriInfo uriInfo) {

		try {
			Author author = convertor
					.getAuthorInfoFromStringUsingXML(authorObj);
			System.out.println(author.getFirstName());
			int id = getNextId();
			author.setId("ID" + String.valueOf(id));
			logger.fine("Creating new author with id: " + id);

			boolean isCreated = db.createAuthor(author);
			if (!isCreated) {
				logger.fine("Could not store author");
				throw new WebApplicationException();
			} else {
				return "<Result>The author is created and stored with id"
						+ author.getId() + "</Result>";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException();
		}
	}

	/**
	 * find an author using the form params posted wiht id
	 * 
	 * @return
	 */

	@GET
	@Path("/createWithURLEncoded")
	@Consumes("application/x-www-form-urlencoded")
	@Produces("application/myJsonContentType")
	public String findWithURLEncoded(@Context UriInfo uriInfo) {
		Author author = null;
		MultivaluedMap<String, String> map = uriInfo.getQueryParameters();
		if (map.isEmpty()) {
			throw new WebApplicationException();
		} else {
			List<String> idList = map.get("id");
			author = db.getAuthor(idList.get(0));
			if (author == null) {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
		return convertor.getAnAuthorJson(author);
	}

	/**
	 * Return a text/xml file
	 * 
	 * @return
	 */
	@GET
	@Path("/file")
	@Produces("text/myTextXML")
	public File getFile() {
		System.out.println("inside");
		File file = null;
		URL baseUrl = this.getClass().getResource("/authors.xml");
		try {
			URI baseUri = baseUrl.toURI();
			file = new File(baseUri);

		} catch (URISyntaxException e) {
			throw new WebApplicationException(e);
		}

		return file;
	}
}
