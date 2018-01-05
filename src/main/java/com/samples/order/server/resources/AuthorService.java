package com.softwareag.mediator.server.resources;

import com.softwareag.mediator.server.AppDB;
import com.softwareag.mediator.server.domain.Address;
import com.softwareag.mediator.server.domain.Author;
import com.sun.jersey.spi.resource.Singleton;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * This is the AuthorService that exposes a bunch of REST endpoints each of which can handle a
 * certain HTTP method and/or specific content-type
 * 
 * @author mgunasek
 */

@Singleton
@Path("/authors")
public class AuthorService {

	private AppDB db;
	private static final Logger logger = Logger.getLogger(AuthorService.class.getPackage().getName());

	private AtomicInteger authorId = new AtomicInteger(4); // start at 4, because, we already have

	// 3 preset authors

	public AuthorService() {
		this.db = AppDB.get();
	}

	private int getNextId() {
		return authorId.getAndIncrement();
	}

	@GET
	@Produces({"application/xml", "application/json"})
	public List<Author> getAllAuthors() {
		logger.fine("Returning all authors...");
		return db.getAuthors();
	}

	/**
	 * Get author info by id
	 * 
	 * @param id
	 */
	@GET
	@Produces({"application/xml", "application/json"})
	@Path("{id}")
	public Author getAuthorByID(@PathParam("id")
	String id) {
		logger.fine("looking up author for id: " + id);
		Author author = db.getAuthor(id);
		if (author == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return author;
	}

	/**
	 * Get author by first or last name
	 * 
	 * @param name -
	 *            either first or last name
	 * @return
	 */
	@GET
	@Path("/template/{name}")
	@Produces({"application/xml", "application/json"})
	public Author getAuthor(@PathParam("name")
	String name) {
		logger.fine("Looking up author using template params; name: " + name);
		Author author = db.getAuthorByName(name);
		if (author == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return author;
	}

	/**
	 * Get author info using first and last name path params
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	@GET
	@Path("/template/{first}-{last}")
	@Produces({"application/xml", "application/json"})
	public Author getAuthor(@PathParam("first")
	String firstName, @PathParam("last")
	String lastName) {
		logger
				.fine("Looking up author using template params; first name: " + firstName + " and last name: "
						+ lastName);
		Author author = db.getAuthorByName(firstName, lastName);
		if (author == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return author;
	}

	/**
	 * Get author info using specified query params in the request uri
	 * 
	 * @param uri
	 * @return
	 */
	@GET
	@Produces({"application/xml", "application/json"})
	@Path("/query")
	public Author getAuthorByQueryParams(@Context
	UriInfo uri) {
		Author author = null;

		MultivaluedMap<String, String> queryParams = uri.getQueryParameters();
		if (queryParams != null) {
			List<String> firstNames = queryParams.get("firstName");
			List<String> lastNames = queryParams.get("lastName");

			if (firstNames != null && lastNames != null) {

				logger.finest("Received " + firstNames.size() + " 'firstName' query params");
				logger.finest("Received " + lastNames.size() + " 'lastName' query params");

				String fname = firstNames.get(0);
				String lname = lastNames.get(0);

				logger.fine("Looking up author using query params; first name: " + fname + " and last name: " + lname);

				author = db.getAuthorByName(fname, lname);
				if (author == null) {
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
			} else if (firstNames != null || lastNames != null) {

				if (firstNames != null)
					logger.finest("Received " + firstNames.size() + " 'firstName' query params");
				else if (lastNames != null)
					logger.finest("Received " + lastNames.size() + " 'lastName' query params");

				String name = null;
				if (firstNames != null)
					name = firstNames.get(0);
				else if (lastNames != null)
					name = lastNames.get(0);

				logger.fine("Looking up author using query params; name: " + name);

				author = db.getAuthorByName(name);
				if (author == null) {
					throw new WebApplicationException(Response.Status.NOT_FOUND);
				}
			} else {
				logger.warning("Either firstName and/or lastName query params are missing in request!");
			}
		} else {
			logger.warning("Query params were not specified! Cannot lookup author info!");
		}

		return author;
	}

	/**
	 * Create a new author based on the incoming &lt;author/&gt; xml
	 * 
	 * @param author
	 * @return
	 */
	@POST
	@Consumes({"application/xml", "application/json"})
	@Path("/create")
	public Response createAuthor(Author author, @Context UriInfo uriInfo) {
		int id = getNextId();
		author.setId("ID" + String.valueOf(id));
		logger.fine("Creating new author with id: " + id);

		boolean isCreated = db.createAuthor(author);
		if (!isCreated) {
			logger.fine("Could not store author");
			throw new WebApplicationException();
		} else {

			// create a new resource as /authors/{id}
			// URI uri =
			// UriBuilder.fromResource(AuthorService.class).path("{id}").build(author.getId());

			URI uri = uriInfo.getBaseUriBuilder().path("authors/{id}").build(author.getId());

			logger.fine("Created author with id: " + author.getId() + " with path: " + uri);
			return Response.created(uri).build();
		}
	}

	/**
	 * Create a new author using the form params posted
	 * 
	 * @param first
	 * @param last
	 * @param middle
	 * @param street
	 * @param city
	 * @param state
	 * @param country
	 * @return
	 */
	@POST
	@Consumes({"application/x-www-form-urlencoded", "application/json"})
	@Path("/createMore")
	public Response createAuthor(@FormParam("first")
	String first, @FormParam("last")
	String last, @FormParam("middle")
	String middle, @FormParam("street")
	String street, @FormParam("city")
	String city, @FormParam("state")
	String state, @FormParam("country")
	String country, @Context
	UriInfo uriInfo) {

		Author author = new Author();
		author.setFirstName(first);
		author.setMiddleName(middle);
		author.setLastName(last);

		Address addr = new Address();
		addr.setState(street);
		addr.setCity(city);
		addr.setState(state);
		addr.setCountry(country);

		author.setAddress(addr);

		return createAuthor(author, uriInfo);

	}

	@PUT
	@Path("{id}")
	@Consumes({"application/xml", "application/json"})
	public void updateAuthor(@PathParam("id")
	String id, Author author) {
		String authorId = author.getId();
		if (authorId != null && id != null & id.equals(authorId)) {
			Author author1 = db.getAuthor(authorId);
			if (author1 != null) {

				db.getAuthors().remove(author1);
				db.getAuthors().add(author);

				logger.fine("Updated author info for author with name: " + author.getFirstName() + ", "
						+ author.getLastName() + " and authorId: " + author.getId());

			} else {
				logger.fine("Could not find an existing author with authorId: " + authorId);
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
		}
	}

	@DELETE
	@Path("{id}")
	public void deleteAuthor(@PathParam("id")
	String id) {
		if ("ID1000".equals(id)) {
			logger.fine("Fake Deleting author with id: " + id);
			return;
		}
		
		Author author = db.getAuthor(id);
		if (author == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		} else {
			logger.fine("Deleting author with id: " + id);
			db.getAuthors().remove(author);
		}

	}

	@GET
	@Path("/file/{filepath: .*}")
	@Produces("text/plain")
	public File getFile(@PathParam("filepath")
	String path) {
		File file = null;
		URL baseUrl = this.getClass().getResource("/");
		try {
			URI baseUri = baseUrl.toURI();

			URI fileUri = UriBuilder.fromUri(baseUri).path(path).build();

			logger.fine("returning file with path: " + fileUri.toString());
			file = new File(fileUri);

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return file;
	}

}
