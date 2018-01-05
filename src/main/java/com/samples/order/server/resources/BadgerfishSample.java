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
 * This is a simple test to test multiroot node support for badgerfish
 * Sample contents can be found in https://iwiki.eur.ad.sag/display/RNDWMGDM/JSON+Multi+Root+Node+support+-+Sample+JSON+objects
 * @author samuel
 *
 */
@Path("/badgerfish")
public class BadgerfishSample {
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
		String jSONContent = "{\"firstName\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"$\":\"sam\"},\"lastName\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"$\":\"Smith\"},\"age\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"$\":\"25\"},\"address\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"}, \"streetAddress\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema2\"},\"$\":\"21 2nd Street\"}, \"city\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema2\"},\"$\":\"New York\"}, \"state\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema2\"},\"$\":\"NY\"}, \"postalCode\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema2\"},\"$\":\"10021\"}},\"phoneNumbers\":[ {\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},  \"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"home\"},  \"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"212 555-1234\"}}, {\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},  \"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"fax\"},  \"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"646 555-4567\"}}]}";
		JSONObject outJSONObj = new JSONObject(jSONContent);
		System.out.println(outJSONObj);
		return Response.status(200).entity(outJSONObj).build();
	}
	
	@DELETE
	@Produces({ "application/json" })
	public Response remove(@QueryParam("multiArray") boolean multiArray) throws JSONException {
		String jSONContent = null;
		System.out.println("multiArray : " + multiArray);
		if (multiArray) {
			jSONContent = "[{\"phoneNumbers\":[{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"home\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"212555-1234\"}},{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"fax\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"646555-4567\"}}]},{\"phoneNumbers\":[{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"home\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"212555-1234\"}},{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"fax\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"646555-4567\"}}]}]";
			JSONArray jsonArray = new JSONArray(jSONContent); 
			return Response.status(200).entity(jsonArray).build();
		} else {
			jSONContent = "{\"phoneNumbers\":[{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"home\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"212 555-1234\"}},{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema1\"},\"type\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"fax\"},\"number\":{\"@xmlns\":{\"$\":\"http://softwareag/sample/schema3\"},\"$\":\"646 555-4567\"}}]}";
			JSONObject outJSONObj = new JSONObject(jSONContent);
			return Response.status(200).entity(outJSONObj).build();
		}
	}
	
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
		return Response.status(200).entity(outJSONObj).build();
	}	
}
