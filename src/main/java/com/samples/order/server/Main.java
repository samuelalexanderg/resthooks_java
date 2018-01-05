package com.softwareag.mediator.server;

import com.sun.grizzly.http.SelectorThread;

/**
 * Start the grizzly embedded http and https servers that hosts the jax-rs resources
 *
 * @author mgunasek
 */
public class Main {

    Server server;
    SSLServer sslServer;

    SelectorThread serverSelector, sslServerSelector;

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        try {
            main.startServers();

            System.out.println("Press Enter to stop the servers.");
            int val = System.in.read();
            main.stopServers(); 
            System.out.println("Done.");
            System.exit(0); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startServers() throws Exception {
        serverSelector = server.startServer();
        sslServerSelector = sslServer.startServer();
    }

    private void stopServers() throws Exception {
        serverSelector.stopEndpoint();
        sslServerSelector.stopEndpoint();
    }

    private void init() {
        //init the data
        AppDB.get();
        CustomerDB.get();

        server = new Server();
        sslServer = new SSLServer();
    }
}
