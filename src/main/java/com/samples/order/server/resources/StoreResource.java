package com.softwareag.mediator.server.resources;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.softwareag.mediator.server.store.LocaleType;
import com.softwareag.mediator.server.store.StartRequestType;
import com.softwareag.mediator.server.store.StartResponseType;
import com.softwareag.mediator.server.store.StartTripType;
import com.softwareag.mediator.server.store.WelcomeMessageType;

@Path("/store")
public class StoreResource {
	@Path("{start}")
	@POST
	@Produces({ "application/xml", "application/json" })
	@Consumes({ "application/xml", "application/json" })
	public Response startTrip(StartRequestType startRequestType) {
		String storeCode = startRequestType.getStoreCode();
		System.out.println("storeCode : " + storeCode);
		StartResponseType startResponseType = new StartResponseType();

		startResponseType.setStatus(200);
		startResponseType.setChecksum(storeCode);

		StartTripType startTripType = new StartTripType();
		startTripType.setSessionID(UUID.randomUUID().toString());

		WelcomeMessageType welcomeMessageType = new WelcomeMessageType();
		welcomeMessageType.setTitle("Welcome Sam!");
		welcomeMessageType
				.setMessage("Please browse an item to start shopping.");
		startTripType.setWelcomeMessage(welcomeMessageType);

		LocaleType localeType = new LocaleType();
		localeType.setCountry("US");
		localeType.setLanguage("en");
		startTripType.setLocale(localeType);

		startResponseType.setStartTrip(startTripType);
		// Just to check native service fault is being sent to client
		if (storeCode.equals("99111")) {
			startResponseType.setStatus(403);
			return Response.status(403).entity(startResponseType).build();
		}

		return Response.status(200).entity(startResponseType).build();
	}
}
