package com.softwareag.mediator.server;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mgunasek
 * @since 1.0 (Feb 16, 2010)
 */
public class Server {

    private static final int DEFAULT_PORT = 9998;

    public static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(getPort(DEFAULT_PORT)).build();

    public Server() {
        init();
    }

    private static int getPort(int defaultPort) {
        String port = System.getenv("HTTP_PORT");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }

    private void init() {
        SelectorThread threadSelector = null;
        try {
            threadSelector = startServer();
            System.out.println(String.format("HTTP Server started! Application WADL available at: "
                    + "%sapplication.wadl", BASE_URI));
//            System.in.read();
//            threadSelector.stopEndpoint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected SelectorThread startServer() throws IOException {
        final Map<String, String> initParams = new HashMap<String, String>();

        initParams.put("com.sun.jersey.config.property.packages",
                "com.softwareag.mediator.server");

        return GrizzlyWebContainerFactory.create(BASE_URI, initParams);
    }
}
