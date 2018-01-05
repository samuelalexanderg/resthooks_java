package com.softwareag.mediator.client;

import com.softwareag.mediator.server.Server;
import com.softwareag.mediator.server.resources.RootResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the root resource of the rest service running on the HTTP server
 *
 * @author mgunasek
 * @since 1.0 (Feb 17, 2010)
 */
public class ServerTest {

    Client client;

    @Before
    public void setup() {
        client = Client.create();
    }


    @Test
    public void testGetRootResource() throws Exception {

        WebResource resource = client.resource(Server.BASE_URI);
        String response = resource.get(String.class);
        assertNotNull(response);
        assertEquals(response, RootResource.CONTENT);

    }

}
