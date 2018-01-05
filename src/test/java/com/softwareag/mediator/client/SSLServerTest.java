package com.softwareag.mediator.client;

import com.softwareag.mediator.server.SSLServer;
import com.softwareag.mediator.server.resources.RootResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.*;

/**
 * Adapted from jersey http client-grizzly server sample
 *
 * @author mgunasek
 * @since 1.0 (Feb 16, 2010)
 */
public class SSLServerTest {
    private final String DEFAULT_PWD = "asdfgh";

    TrustManager mytm[] = null;
    KeyManager mykm[] = null;


    @Before
    public void setUp() throws Exception {
        File tsFile = FileUtils.toFile(getClass().getResource("/truststore_client"));
        File ksFile = FileUtils.toFile(getClass().getResource("/keystore_client"));

        try {
            mytm = new TrustManager[]{new MyX509TrustManager(tsFile, DEFAULT_PWD.toCharArray())};
            mykm = new KeyManager[]{new MyX509KeyManager(ksFile, DEFAULT_PWD.toCharArray())};
        } catch (Exception ex) {
            System.out.println("Something bad happened " + ex.getMessage());
        }
    }


    @Test
    public void testSSLWithBasicAuth() {

        SSLContext context = null;

        try {
            context = SSLContext.getInstance("SSL");
            context.init(mykm, mytm, null);
        } catch (NoSuchAlgorithmException nae) {

        } catch (KeyManagementException kme) {

        }

        HTTPSProperties prop = new HTTPSProperties(null, context);

        DefaultClientConfig dcc = new DefaultClientConfig();
        dcc.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);

        Client c = Client.create(dcc);

        // client basic auth demonstration
        c.addFilter(new HTTPBasicAuthFilter("user", "password"));

        System.out.println("Client: GET " + SSLServer.BASE_URI);

        WebResource r = c.resource(SSLServer.BASE_URI);

        String page = r.path("/").get(String.class);

        System.out.println("Got response: " + page);

        assertEquals(RootResource.CONTENT, page);
    }

    /**
     * Test to see that HTTP 401 is returned when client tries to GET without
     * proper credentials.
     */

    @Test
    public void testSslNoBasicAuth() {

        SSLContext context = null;

        try {
            context = SSLContext.getInstance("SSL");
            context.init(mykm, mytm, null);
        } catch (NoSuchAlgorithmException nae) {
            System.out.println("NoSuchAlgorithmException " + nae.getMessage());
        } catch (KeyManagementException kme) {
            System.out.println("KeyManagementException happened " + kme.getMessage());

        }


        HTTPSProperties prop = new HTTPSProperties(null, context);

        DefaultClientConfig dcc = new DefaultClientConfig();
        dcc.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);

        Client c = Client.create(dcc);

        WebResource r = c.resource(SSLServer.BASE_URI);

        String msg = null;

        try {
            String page = r.path("/").get(String.class);
            System.out.println("Got response: " + page);
        } catch (Exception e) {
            msg = e.getMessage();
        }

        assertNotNull(msg);
        assertTrue(msg.contains("401"));
    }

    /**
     * Test to see that ClientHandlerException is caught (SSLHandshakeException is thrown on server)
     * when client doesn't send cert during ssl handshake.
     */
    @Test(expected = ClientHandlerException.class)
    public void testSSLAuthFailure() {

        SSLContext context = null;

        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, mytm, null);           //no key manager
        } catch (NoSuchAlgorithmException nae) {
            System.out.println("NoSuchAlgorithmException " + nae.getMessage());
        } catch (KeyManagementException kme) {
            System.out.println("KeyManagementException happened " + kme.getMessage());
        }

        HTTPSProperties prop = new HTTPSProperties(null, context);

        DefaultClientConfig dcc = new DefaultClientConfig();
        dcc.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);

        Client c = Client.create(dcc);

        WebResource r = c.resource(SSLServer.BASE_URI);

        String page = r.path("/").get(String.class);
    }

}

/**
 * Taken from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
 */
class MyX509TrustManager implements X509TrustManager {

    /*
    * The default PKIX X509TrustManager9.  We'll delegate
    * decisions to it, and fall back to the logic in this class if the
    * default X509TrustManager doesn't trust it.
    */
    X509TrustManager pkixTrustManager;

    MyX509TrustManager(File trustStore, char[] password) throws Exception {
        // create a "default" JSSE X509TrustManager.

        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream(trustStore), password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(ks);

        TrustManager tms[] = tmf.getTrustManagers();

        /*
        * Iterate over the returned trustmanagers, look
        * for an instance of X509TrustManager.  If found,
        * use that as our "default" trust manager.
        */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                pkixTrustManager = (X509TrustManager) tms[i];
                return;
            }
        }

        /*
        * Find some other way to initialize, or else we have to fail the
        * constructor.
        */
        throw new Exception("Couldn't initialize");
    }

    /*
    * Delegate to the default trust manager.
    */

    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            pkixTrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.
        }
    }

    /*
    * Delegate to the default trust manager.
    */

    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            pkixTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
            * Possibly pop up a dialog box asking whether to trust the
            * cert chain.
            */
        }
    }

    /*
    * Merely pass this through.
    */

    public X509Certificate[] getAcceptedIssuers() {
        return pkixTrustManager.getAcceptedIssuers();
    }
}

/**
 * Inspired from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
 */
class MyX509KeyManager implements X509KeyManager {

    /*
    * The default PKIX X509KeyManager.  We'll delegate
    * decisions to it, and fall back to the logic in this class if the
    * default X509KeyManager doesn't trust it.
    */
    X509KeyManager pkixKeyManager;

    MyX509KeyManager(File keyStore, char[] password) throws Exception {
        // create a "default" JSSE X509KeyManager.

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStore), password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
        kmf.init(ks, "asdfgh".toCharArray());

        KeyManager kms[] = kmf.getKeyManagers();

        /*
        * Iterate over the returned keymanagers, look
        * for an instance of X509KeyManager.  If found,
        * use that as our "default" key manager.
        */
        for (int i = 0; i < kms.length; i++) {
            if (kms[i] instanceof X509KeyManager) {
                pkixKeyManager = (X509KeyManager) kms[i];
                return;
            }
        }

        /*
        * Find some other way to initialize, or else we have to fail the
        * constructor.
        */
        throw new Exception("Couldn't initialize");
    }

    public PrivateKey getPrivateKey(String arg0) {
        return pkixKeyManager.getPrivateKey(arg0);
    }

    public X509Certificate[] getCertificateChain(String arg0) {
        return pkixKeyManager.getCertificateChain(arg0);
    }

    public String[] getClientAliases(String arg0, Principal[] arg1) {
        return pkixKeyManager.getClientAliases(arg0, arg1);
    }

    public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
        return pkixKeyManager.chooseClientAlias(arg0, arg1, arg2);
    }

    public String[] getServerAliases(String arg0, Principal[] arg1) {
        return pkixKeyManager.getServerAliases(arg0, arg1);
    }

    public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
        return pkixKeyManager.chooseServerAlias(arg0, arg1, arg2);
    }
}


