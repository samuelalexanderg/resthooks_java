package com.samples.order.server;

import com.softwareag.mediator.server.domain.Author;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In-memory db for the application
 *
 * @author mgunasek
 */
public class AppDB {

    private String authorsXmlFilePath = "/authors.xml";

    private JAXBContext ctx;

    private Unmarshaller unmarshaller;
    private Marshaller marshaller;

    private List<Author> authors;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private static final AppDB instance = new AppDB();

    public static AppDB get() {
        return instance;
    }


    protected AppDB() {
        initJaxb();
        this.authors = getAuthorsFromFile();
        logger.fine("Initialized app db...");
    }


    public List<Author> getAuthors() {
        if (this.authors == null) {
            this.authors = Collections.emptyList();
        }
        return this.authors;
    }

    public Author getAuthor(String id) {
        Author author = null;
        for (Author a: getAuthors()) {
            if (a.getId().equals(id)) {
                author = a;
                break;
            }
        }
        return author;
    }
    
    public Author getAuthorByName(String name) {
        Author author = null;
        for (Author a: getAuthors()) {
            if (a.getFirstName().equalsIgnoreCase(name) ||
                     a.getLastName().equalsIgnoreCase(name)) {
                author = a;
                break;
            }
        } 
        return author;
    }

    public Author getAuthorByName(String first, String last) {
        Author author = null;
        for (Author a: getAuthors()) {
            if (a.getFirstName().equalsIgnoreCase(first) &&
                     a.getLastName().equalsIgnoreCase(last)) {
                author = a;
                break;
            }
        } 
        return author;
    }

    public boolean createAuthor(Author author) {
        this.authors.add(author);
        return storeAuthors();
    }

    protected void initJaxb() {
        try {
            ctx = JAXBContext.newInstance(AuthorsHolder.class);
            unmarshaller = ctx.createUnmarshaller();
            marshaller = ctx.createMarshaller();

        } catch (JAXBException e) {
            logger.log(Level.SEVERE, "JAXB error", e);
        }
    }


    protected boolean storeAuthors() {
        boolean isStored = false;
        AuthorsHolder holder = new AuthorsHolder();
        holder.authors = authors;
        
        URL url = this.getClass().getResource(authorsXmlFilePath);
        if (url!=null) {
            try {
                File file = new File(url.toURI());
                if (file.exists()) {
                    marshaller.marshal(holder, file);

                    isStored = true;
                }
                else {
                    logger.warning("Could not locate file: " + authorsXmlFilePath);
                }
            } catch (URISyntaxException e) {
                logger.log(Level.WARNING, "", e);
            } catch (JAXBException e) {
                logger.log(Level.SEVERE, "JAXB error", e);
            }
        }
        else {
            logger.warning("Could not get resource: " + authorsXmlFilePath);
        }

        return isStored;
    }

    protected List<Author> getAuthorsFromFile() {
        List<Author> authorList = null;
        InputStream is = this.getClass().getResourceAsStream(authorsXmlFilePath);
        if (is != null) {
            try {
                Object o = unmarshaller.unmarshal(is);
                if (o != null && o instanceof AuthorsHolder) {
                    authorList = ((AuthorsHolder) o).authors;
                    if (authorList!=null) {
                        logger.info("Loaded " + authorList.size() + " authors from file: " + authorsXmlFilePath);
                    }
                    else {
                        logger.warning("authors info is null!");
                    }

                } else {
                    logger.warning("Could not load authors information!");
                }

            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
        return authorList;
    }


}


