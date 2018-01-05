package com.softwareag.mediator.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
/**
 * This is a simple test to test multiroot node support
 * Sample contents can be found in https://iwiki.eur.ad.sag/display/RNDWMGDM/JSON+Multi+Root+Node+support+-+Sample+JSON+objects
 * @author samuel
 *
 */
@Path("/multi")
public class MultiRootNodeResource {
	
	/**
	 * This method will accpet any json object and return the same json object
	 * A Json object with multiroot node can be sent to this method
	 * @param inJSONObj
	 * @return
	 * @throws JSONException
	 */
	@POST
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response print(JSONObject inJSONObj) throws JSONException {
		// just reading the keys and values 
		// Constructing a json object with same values.
		// This is not needed we can just return jsonObject
		// But Just like that it is done
		String jSONContent = inJSONObj.toString();
		System.out.println(jSONContent);
		JSONObject outJSONObj = new JSONObject(jSONContent);
		if ((jSONContent.contains("99111") || jSONContent.contains("sam")) && !jSONContent.contains("sample")) {
			return Response.status(403).entity(outJSONObj).build();
		}
		return Response.status(200).entity(outJSONObj).build();
	}
	
	/**
	 * This method will accpet any json object and return the same json object
	 * A Json object with multiroot node can be sent to this method
	 * @param inJSONObj
	 * @return
	 * @throws JSONException
	 */
	@PUT
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response add(JSONObject inJSONObj) throws JSONException {
		// just reading the keys and values 
		// Constructing a json object with same values.
		// This is not needed we can just return jsonObject
		// But Just like that it is done
		String jSONContent = inJSONObj.toString();
		System.out.println(jSONContent);
		JSONObject outJSONObj = new JSONObject(jSONContent);
		if (jSONContent.contains("99111") || jSONContent.contains("sam")) {
			return Response.status(403).entity(outJSONObj).build();
		}
		return Response.status(200).entity(outJSONObj).build();
	}
	
	/**
	 * This method will accpet any json object and return the same json object
	 * A Json object with multiroot node can be sent to this method
	 * @param inJSONObj
	 * @return
	 * @throws JSONException
	 */
	@DELETE
	@Produces({ "application/json" })
	public Response remove() throws JSONException {
		String jSONContent = "{\n   \"checksum\": \"99111\",\n   \"startTrip\":    {\n      \"locale\":       {\n         \"country\": \"US\",\n         \"language\": \"en\"\n      },\n      \"sessionID\": \"75eb3473-2a3a-4282-9bd5-d240e15a9882\",\n      \"welcomeMessage\":       {\n         \"message\": \"Please browse an item to start shopping.\",\n         \"title\": \"Welcome Sam!\"\n      }\n   },\n   \"status\": \"403\"\n}";
		JSONObject outJSONObj = new JSONObject(jSONContent);
		System.out.println(outJSONObj);
		return Response.status(200).entity(outJSONObj).build();
	}
	
	/**
	 * This method will accpet any json object and return the same json object
	 * A Json object with multiroot node can be sent to this method
	 * @param inJSONObj
	 * @return
	 * @throws JSONException
	 */
	@GET
	@Produces({ "application/json" })
	public Response fetch() throws JSONException {
		String jSONContent = "{\n   \"checksum\": \"99111\",\n   \"startTrip\":    {\n      \"locale\":       {\n         \"country\": \"US\",\n         \"language\": \"en\"\n      },\n      \"sessionID\": \"75eb3473-2a3a-4282-9bd5-d240e15a9882\",\n      \"welcomeMessage\":       {\n         \"message\": \"Please browse an item to start shopping.\",\n         \"title\": \"Welcome Sam!\"\n      }\n   },\n   \"status\": \"403\"\n}";
		JSONObject outJSONObj = new JSONObject(jSONContent);
		System.out.println(outJSONObj);
		return Response.status(200).entity(outJSONObj).build();
	}	
}
