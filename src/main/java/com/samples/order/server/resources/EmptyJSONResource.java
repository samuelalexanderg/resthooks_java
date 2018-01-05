package com.softwareag.mediator.server.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Simple test to test empty JSON content
 * Sample contents can be found in https://iwiki.eur.ad.sag/display/RNDWMGDM/JSON+Multi+Root+Node+support+-+Sample+JSON+objects
 * @author samuel
 *
 */
@Path("empty")
public class EmptyJSONResource {
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
		return Response.status(200).entity(outJSONObj).build();
	}
	
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
		return Response.status(200).entity(outJSONObj).build();
	}
	
	@DELETE
	@Produces({ "application/json" })
	public Response remove(@QueryParam("withKey") String withKey) throws JSONException {
		String jSONContent = null; 
		if (withKey.equals("true")) {
			jSONContent = "{\"someKey\":\"\"}";
		} else {
			jSONContent = "{}";
		}
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
	public Response fetch(@QueryParam("withKey") String withKey) throws JSONException {
		String jSONContent = null; 
		if (withKey.equals("true")) {
			jSONContent = "{\"someKey\":\"\"}";
		} else {
			jSONContent = "{}";
		}
		JSONObject outJSONObj = new JSONObject(jSONContent);
		System.out.println(outJSONObj);
		return Response.status(200).entity(outJSONObj).build();
	}	
}
