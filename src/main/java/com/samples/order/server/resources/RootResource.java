package com.softwareag.mediator.server.resources;

import com.sun.jersey.core.util.Base64;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author mgunasek
 * @since 1.0 (Feb 16, 2010)
 */
@Path("/")
public class RootResource {

    public static final String CONTENT = "Hello Jersey";

    @GET
    public Response get1(@Context HttpHeaders headers) {
        System.out.println("Service: GET / User: " + getUser(headers));

        return Response.ok(CONTENT).type(MediaType.TEXT_HTML).build();
    }

    private String getUser(HttpHeaders headers) {
        String user = null;

        List<String> authHdr = headers.getRequestHeader("authorization");
        String[] values = new String[0];
        if (authHdr!=null && !authHdr.isEmpty()) {
            String auth = authHdr.get(0);

            auth = auth.substring("Basic ".length());
            values = new String(Base64.base64Decode(auth)).split(":");
            user = values[0];
        }

        return user!=null? user: "unknown; No Basic Auth header";
    }
}

