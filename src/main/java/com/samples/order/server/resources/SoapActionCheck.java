package com.softwareag.mediator.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
/**
 * This is the simple test to check SOAPAction header present or not
 * @author KAMU
 *
 */
@Path("/soapheader")
public class SoapActionCheck  {
	/**
	 * This method accept the SOAPAction header value
	 * SOAPAction value is null means OK
	 * Otherwise Forbidden
	 * @param soapAction
	 * @return
	 * @throws JSONException 
	 */
	@POST
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	@Path("/check")
	public Response getTutorial(@HeaderParam("SOAPAction") String soapAction) throws JSONException {
		String fail = "{\"failedJSONObject\":\"SOAPAction header passed to native service\"}";
		String success = "{\"successJSONObject\":\"SOAPAction header not passed to native service\"}";
		JSONObject failedJSONObj = new JSONObject(fail);
		JSONObject successJSONObj=new JSONObject(success);
		/**
		 * Just check SOAPAction header value is null or not
		 */
		if (soapAction != null) {
			return Response.status(403).entity(failedJSONObj+" : "+soapAction).build();
		}
		return Response.status(200).entity(successJSONObj).build();
	}
}
