package com.softwareag.mediator.server;

import com.softwareag.mediator.server.auth.SecurityFilter;
import com.sun.grizzly.SSLConfig;
import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.grizzly.ssl.SSLSelectorThread;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URL;

/**
 * Based on the jersey-grizzly https client server sample
 *
 * @author mgunasek
 * @since 1.0 (Feb 16, 2010)
 */
public class SSLServer {

    public static final URI BASE_URI = getBaseURI();
    
    private final String DEFAULT_PWD = "asdfgh";

    private static URI getBaseURI() {
        return UriBuilder.fromUri("https://localhost/").port(getPort(4463)).build();
    }

    private static int getPort(int defaultPort) {
        String port = System.getenv("HTTPS_PORT");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }

    protected SelectorThread startServer() {
        GrizzlyWebServer webServer = new GrizzlyWebServer(getPort(4463), ".", true);

        // add Jersey resource servlet

        ServletAdapter jerseyAdapter = new ServletAdapter();
        jerseyAdapter.addInitParameter("com.sun.jersey.config.property.packages",
                "com.softwareag.mediator.server.resources;" +  //contains the @Path resources
                "com.softwareag.mediator.server.auth");        //contains the @Provider resource
        jerseyAdapter.setContextPath("/");
        jerseyAdapter.setServletInstance(new ServletContainer());

        // add security filter (which handles http basic authentication)

        jerseyAdapter.addInitParameter(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, SecurityFilter.class.getName());

        webServer.addGrizzlyAdapter(jerseyAdapter, new String[]{"/"});


        // Grizzly ssl configuration

        SSLConfig sslConfig = new SSLConfig();

        // sslConfig.setNeedClientAuth(true); // doesn't work - known grizzly bug, will be fixed in 2.0.0

        // set up security context
        URL ksUrl = getClass().getResource("/keystore_server");
        sslConfig.setKeyStoreFile(ksUrl.getPath()); // contains server keypair
        sslConfig.setKeyStorePass(DEFAULT_PWD);

        URL tsUrl = getClass().getResource("/truststore_server");
        sslConfig.setTrustStoreFile(tsUrl.getPath()); // contains client certificate
        sslConfig.setTrustStorePass(DEFAULT_PWD);

        webServer.setSSLConfig(sslConfig);

        // turn server side client certificate authentication on

        SelectorThread selectorThread = webServer.getSelectorThread();
        ((SSLSelectorThread) selectorThread).setNeedClientAuth(true);

        try {
            // start Grizzly embedded server //
//            System.out.println("Starting Grizzly on SSL...");
            webServer.start();
            System.out.println("HTTPS Server started at: " + BASE_URI); 
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return selectorThread;
    }
}
