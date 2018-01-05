package com.softwareag.mediator.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/SMGME2730")
public class SMGME2730 {

	@POST
	@Produces({ "application/xml" })
	@Consumes({ "application/xml" })
	public Response print(Object input) {
		System.out.println(input);
		return Response.status(200).entity(input).build();
	}
	
	@GET
	@Consumes({ "application/xml" })
	public Response fetch() {
		String xml = "<SMGME2730>Sample service to test SMGME2730</SMGME2730>";
		System.out.println(xml);
		return Response.status(200).entity(xml).build();
	}
}
