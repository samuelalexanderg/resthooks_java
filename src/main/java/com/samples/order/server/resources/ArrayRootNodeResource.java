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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Simple test to test array root node support
 * Sample contents can be found in https://iwiki.eur.ad.sag/display/RNDWMGDM/JSON+Multi+Root+Node+support+-+Sample+JSON+objects
 * @author samuel
 *
 */
@Path("array")
public class ArrayRootNodeResource {
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
	public Response print(JSONArray inJSONArray) throws JSONException {
		// just reading the keys and values 
		// Constructing a json object with same values.
		// This is not needed we can just return jsonObject
		// But Just like that it is done
		String jSONContent = inJSONArray.toString();
		System.out.println(jSONContent);
		JSONArray outJSONArr = new JSONArray(jSONContent);
		if (jSONContent.contains("99111") || jSONContent.contains("000 000-0000")) {
			return Response.status(403).entity(outJSONArr).build();
		}
		return Response.status(200).entity(outJSONArr).build();
	}
	
	@PUT
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response add(JSONArray inJSONArray) throws JSONException {
		// just reading the keys and values 
		// Constructing a json object with same values.
		// This is not needed we can just return jsonObject
		// But Just like that it is done
		String jSONContent = inJSONArray.toString();
		System.out.println(jSONContent);
		JSONArray outJSONArr = new JSONArray(jSONContent);
		if (jSONContent.contains("99111") || jSONContent.contains("000 000-0000")) {
			return Response.status(403).entity(outJSONArr).build();
		}
		return Response.status(200).entity(outJSONArr).build();
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
	public Response fetch(@QueryParam("withKey") boolean withKey, @QueryParam("multiArray") boolean multiArray) throws JSONException {
		String jSONContent = null;
		System.out.println("withKey : " + withKey);
		if (withKey) {
			if (multiArray) {
				jSONContent = "[{    \"phoneNumbers\": [        {            \"type\": \"home\",            \"number\": \"212 555-1234\"        },        {            \"type\": \"fax\",            \"number\": \"646 555-4567\"        }    ]},{    \"phoneNumbers\": [        {            \"type\": \"home\",            \"number\": \"212 555-1234\"        },        {            \"type\": \"fax\",            \"number\": \"646 555-4567\"        }    ]}]";
			} else {
				jSONContent = "{\n    \"phoneNumbers\": [\n        {\n            \"type\": \"home\",\n            \"number\": \"212 555-1234\"\n        },\n        {\n            \"type\": \"fax\",\n            \"number\": \"646 555-4567\"\n        }\n    ]\n}";
			}
			JSONObject outJSONObj = new JSONObject(jSONContent);
			System.out.println(outJSONObj);
			return Response.status(200).entity(outJSONObj).build();
		} else {
			if (multiArray) {
				jSONContent = "[[    {        \"type\": \"home\",        \"number\": \"212 555-1234\"    },    {        \"type\": \"fax\",        \"number\": \"646 555-4567\"    }],[    {        \"type\": \"home\",        \"number\": \"212 555-1234\"    },    {        \"type\": \"fax\",        \"number\": \"646 555-4567\"    }]]";
			} else {
				jSONContent = "[\n    {\n        \"type\": \"home\",\n        \"number\": \"212 555-1234\"\n    },\n    {\n        \"type\": \"fax\",\n        \"number\": \"646 555-4567\"\n    }\n]";
			}
			JSONArray outJSONObj = new JSONArray(jSONContent);
			System.out.println(outJSONObj);
			return Response.status(200).entity(outJSONObj).build();
		}
	}
	
	@DELETE
	@Produces({ "application/json" })
	public Response remove(@QueryParam("withKey") boolean withKey, @QueryParam("multiArray") boolean multiArray) throws JSONException {
		String jSONContent = null;
		System.out.println("withKey : " + withKey);
		System.out.println("multiArray : " + multiArray);
		if (withKey) {
			if (multiArray) {
				jSONContent = "[{    \"phoneNumbers\": [        {            \"type\": \"home\",            \"number\": \"212 555-1234\"        },        {            \"type\": \"fax\",            \"number\": \"646 555-4567\"        }    ]},{    \"phoneNumbers\": [        {            \"type\": \"home\",            \"number\": \"212 555-1234\"        },        {            \"type\": \"fax\",            \"number\": \"646 555-4567\"        }    ]}]";
			} else {
				jSONContent = "{\n    \"phoneNumbers\": [\n        {\n            \"type\": \"home\",\n            \"number\": \"212 555-1234\"\n        },\n        {\n            \"type\": \"fax\",\n            \"number\": \"646 555-4567\"\n        }\n    ]\n}";
			}
			JSONObject outJSONObj = new JSONObject(jSONContent);
			System.out.println(outJSONObj);
			return Response.status(200).entity(outJSONObj).build();
		} else {
			if (multiArray) {
				jSONContent = "[[    {        \"type\": \"home\",        \"number\": \"212 555-1234\"    },    {        \"type\": \"fax\",        \"number\": \"646 555-4567\"    }],[    {        \"type\": \"home\",        \"number\": \"212 555-1234\"    },    {        \"type\": \"fax\",        \"number\": \"646 555-4567\"    }]]";
			} else {
				jSONContent = "[\n    {\n        \"type\": \"home\",\n        \"number\": \"212 555-1234\"\n    },\n    {\n        \"type\": \"fax\",\n        \"number\": \"646 555-4567\"\n    }\n]";
			}
			JSONArray outJSONObj = new JSONArray(jSONContent);
			System.out.println(outJSONObj);
			return Response.status(200).entity(outJSONObj).build();
		}
	}	
}
