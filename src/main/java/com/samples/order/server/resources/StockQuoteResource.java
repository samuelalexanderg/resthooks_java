package com.softwareag.mediator.server.resources;

import com.softwareag.mediator.server.stockquote.GetQuote;
import com.softwareag.mediator.server.stockquote.GetQuoteResponse;
import com.softwareag.mediator.server.stockquote.ObjectFactory;
import com.softwareag.mediator.server.stockquote.PlaceOrder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mgunasek
 * @since 1.0 (Apr 21, 2010)
 */

@Path("/stockquote")
public class StockQuoteResource {

    private static final Logger logger = Logger.getLogger(StockQuoteResource.class.getPackage().getName());

    private ObjectFactory objFac;
    private Marshaller marshaller;
    private JAXBContext jaxbCtx;

    public static final String CONTENT = "<h2>Hello from StockQuote JAX-RS service</h2>";

    public StockQuoteResource() {
        objFac = new ObjectFactory();
        try {
            jaxbCtx = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "Error initializing JAXB context", e);
        }
    }

    @GET
    public Response get() {
        return Response.ok(CONTENT).type(MediaType.TEXT_HTML).build();
    }


    @GET
    @Path("/quotes/{symbol}")
    @Produces("application/json")
    public GetQuoteResponse getQuote(@PathParam("symbol") String symbol) {

        if (symbol == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        logger.info("Generating quote for symbol [" + symbol + "]");
        GetQuoteResponse response = generateQuoteResponse(symbol);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Generated response: ");
            dumpObj(response);
        }

        return response;
    }

    @GET
    @Path("/text/quotes")
    @Produces("text/xml")
    public String getQuoteAsTextXml(@QueryParam("symbol") String symbol) {
        logger.fine("Received symbol:  " + symbol);
        String responseXml = "<result>Missing 'symbol' query param</result>";
        if (symbol != null) {
            GetQuoteResponse response = getQuote(symbol);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            dumpObject(response, baos);

            responseXml = baos.toString();
        }
        return responseXml;
    }


    @GET
    @Path("/quotes")
    public GetQuoteResponse getQuoteQueryParam(@QueryParam("symbol") String symbol) {
        return getQuote(symbol);
    }


    @POST
    @Path("/order")
    @Consumes({"application/xml", "application/json"})
    public Response placeOrder(PlaceOrder order) {
        System.out.println("Got placeorder request!");
        if (order != null) {
            logger.info("Received order: [symbol = " + order.getSymbol() + "; Price = " + order.getPrice()
                    + "; Quantity = " + order.getQuantity() + "]");

            return Response.status(Response.Status.ACCEPTED).build();
        }

        throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }


    private GetQuoteResponse generateQuoteResponse(String symbol) {
        GetQuoteResponse res = objFac.createGetQuoteResponse();

        res.setSymbol(symbol);
        res.setLast(getRandom(100, 0.9, true));
        res.setLastTradeTimestamp(new Date().toString());
        res.setChange(getRandom(3, 0.5, false));
        res.setHigh(getRandom(res.getLast(), 0.05, false));
        res.setLow(getRandom(res.getLast(), 0.05, false));
        res.setVolume((int) getRandom(10000, 1.0, true));
        res.setMarketCap(getRandom(10E6, 5.0, false));
        res.setPrevClose(getRandom(res.getLast(), 0.15, false));
        res.setPercentageChange(res.getChange() / res.getPrevClose() * 100);
        res.setEarnings(getRandom(10, 0.4, false));
        res.setName(symbol + " Company");
        return res;

    }

    private static double getRandom(double base, double varience, boolean onlypositive) {
        double rand = Math.random();
        return (base + ((rand > 0.5 ? 1 : -1) * varience * base * rand))
                * (onlypositive ? 1 : (rand > 0.5 ? 1 : -1));
    }


    private void dumpObj(Object o) {
        dumpObject(o, System.out);
    }

    private void dumpObject(Object o, OutputStream out) {
        try {
            if (marshaller == null) {
                marshaller = jaxbCtx.createMarshaller();
            }
            marshaller.marshal(o, out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/file/{filepath: .*}")
    @Produces("text/plain")
    public File getFile(@PathParam("filepath") String path) {
        File file = null;
        URL baseUrl = this.getClass().getResource("/");
        try {
            URI baseUri = baseUrl.toURI();

            URI fileUri = UriBuilder.fromUri(baseUri).path(path).build();

            logger.fine("returning file with path: " + fileUri.toString());
            file = new File(fileUri);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return file;
    }


    public static void main(String[] args) {
        GetQuote quote = new GetQuote();
        quote.setSymbol("webm");

        StockQuoteResource resource = new StockQuoteResource();
        System.out.println("-----");
        resource.dumpObj(quote);

        GetQuoteResponse getQuoteResponse = resource.generateQuoteResponse("IBM");

        System.out.println("------ get quote response -----");
        resource.dumpObj(getQuoteResponse);

        PlaceOrder po = new PlaceOrder();
        po.setSymbol("IBM");
        po.setPrice(1231.123);
        po.setQuantity(100);
        System.out.println("------ place order xml -----");
        resource.dumpObj(po);
    }

}
