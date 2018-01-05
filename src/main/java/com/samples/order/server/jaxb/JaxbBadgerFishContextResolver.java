package com.softwareag.mediator.server.jaxb;

import com.softwareag.mediator.server.domain.Author;
import com.softwareag.mediator.server.stockquote.GetQuote;
import com.softwareag.mediator.utils.PackageUtil;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns a custom {@link com.sun.jersey.api.json.JSONJAXBContext} configured to use
 * the badgerfish json notation for the rest resources
 *
 * @author mgunasek
 * @since 6/21/11
 */

@Provider
public class JaxbBadgerFishContextResolver implements ContextResolver<JAXBContext> {

//    public static final String PKG_AUTHORS = Author.class.getPackage().getName();
    public static final String PKG_STOCKQUOTE = GetQuote.class.getPackage().getName();
    JSONJAXBContext jaxbCtx;
    List<Class> allClasses = new ArrayList<Class>();

    public JaxbBadgerFishContextResolver() {
        try {

//            List<Class> authorsClasses = PackageUtil.getClasses(PKG_AUTHORS);
//            if (authorsClasses!=null && !authorsClasses.isEmpty() ) {
//                allClasses.addAll(authorsClasses);
//            }

            // Making an assumption that:
            // only the stockquote classes will use the BadgerFish notation for application/json media type
            // the authors resource is left to use the default mapped notation

            List<Class> stockQuoteClasses = PackageUtil.getClasses(PKG_STOCKQUOTE);
            if (stockQuoteClasses!=null && !stockQuoteClasses.isEmpty())  {
                allClasses.addAll(stockQuoteClasses);
            }

            dumpClassesInContext();

            jaxbCtx = new JSONJAXBContext(getBadgerFishJsonConfig(),
                                            allClasses.toArray(new Class[allClasses.size()]));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private void dumpClassesInContext() {
        if (!allClasses.isEmpty()) {
            System.out.println("Added the following classes for custom JAXBContext using BadgerFish notation:");
            for (Class clz : allClasses) {
                System.out.println(clz.getName());
            }
        }
        else System.out.println(" !! Did not find any classes to add for custom JAXBContext using BadgerFish notation !!");
    }

    private JSONConfiguration getBadgerFishJsonConfig() {
        JSONConfiguration.Builder builder = JSONConfiguration.badgerFish();
        return builder.build();
    }


    @Override
    public JAXBContext getContext(Class<?> type) {
        return (type!=null && allClasses.contains(type))? jaxbCtx : null;
    }
}
