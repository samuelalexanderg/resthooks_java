package com.softwareag.mediator.server.resources;

import com.softwareag.mediator.server.AuthorsHolder;
import com.softwareag.mediator.server.Server;
import com.softwareag.mediator.server.domain.Address;
import com.softwareag.mediator.server.domain.Author;
import com.softwareag.mediator.server.domain.Book;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.JAXBContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test all the available endpoints for the AuthorService REST service
 *
 * @author mgunasek
 * @since 1.0 (Feb 17, 2010)
 */
public class AuthorServiceTest {

    private static Client client;

    private static JAXBContext jaxbCtx;

    private WebResource resource;

    @BeforeClass
    public static void setUpClass() throws Exception {
        client = Client.create();
        jaxbCtx = JAXBContext.newInstance(AuthorsHolder.class);
        assertNotNull(jaxbCtx);
    }

    @Before
    public void setup() throws Exception {
        resource = client.resource(Server.BASE_URI);
        assertNotNull(resource);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        client.destroy();
    }

    @Test
    public void testGetAllAuthors() throws Exception {

        resource = resource.path("authors");

        System.out.println("Fetching URI: " + resource.getURI());

        AuthorsHolder authorsHolder = resource.get(AuthorsHolder.class);

        List<Author> authorList = authorsHolder.getAuthors();
        assertNotNull(authorList);
        assertFalse(authorList.isEmpty());
        assertTrue(authorList.size() == 3);   //initially there should be three authors

    }

    @Test
    public void testGetAuthor() throws Exception {
        String authorId = "ID1";

        resource = resource.path("authors").path(authorId);
        System.out.println("Fetching URI: " + resource.getURI());

        Author author = resource.get(Author.class);
        assertNotNull(author);

        assertEquals(author.getId(), authorId);

    }

    @Test
    public void testGetAuthorByQueryParams() throws Exception {

        String firstName = "Isaac";
        String lastName = "Asimov";

        MultivaluedMapImpl queryParams = new MultivaluedMapImpl();
        queryParams.addFirst("firstName", firstName);
        queryParams.add("lastName", lastName);

        resource = resource.path("authors").path("query").queryParams(queryParams);

        System.out.println("Fetching URI: " + resource.getURI());

        Author author = resource.get(Author.class);
        assertNotNull(author);

        assertTrue(author.getFirstName().equals(firstName));
        assertTrue(author.getLastName().equals(lastName));

    }

    @Test
    public void testGetAuthorByTemplateParams() throws Exception {

        String firstName = "Isaac";
        String lastName = "Asimov";

        URI uri = resource.getUriBuilder()
                .path("authors").path("template")
                .path("{first}-{last}")
                .build(firstName, lastName);

        resource = resource.uri(uri);

        System.out.println("Fetching URI: " + resource.getURI());

        Author author = resource.get(Author.class);
        assertNotNull(author);

        assertTrue(author.getFirstName().equals(firstName));
        assertTrue(author.getLastName().equals(lastName));

    }

    @Test
    public void testCreateAuthorWithPostXml() throws Exception {
        Author author = new Author();
        author.setFirstName("J.R.R");
        author.setLastName("Tolkien");

        Address addr = new Address();
        addr.setStreet("unknown");
        addr.setCity("Bournemouth");
        addr.setState("");
        addr.setCountry("England");

        author.setAddress(addr);

        Book book = new Book();
        book.setTitle("The Fellowship of the Ring");
        book.setYear(1954);
        book.setIsbn("0618574948");

        author.getBooks().add(book);

        ClientResponse response = resource.path("authors").path("create").post(ClientResponse.class, author);

        assertNotNull(response);

        assertEquals(response.getStatus(), 201);  //201 for created

        String contentLoc = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertNotNull(contentLoc);

        System.out.println("Got new resource location = " + contentLoc);

    }

    @Test
    public void testUpdateAuthor() throws Exception {

        String authorId = "ID3"; //author = dan brown

        //get the Author from the service
        WebResource res = resource.path("authors").path(authorId);
        Author author = res.get(Author.class);
        assertNotNull(author);

        assertTrue(author.getId().equals(authorId));

        //Add a new book for this author
        Book book = new Book();
        book.setTitle("Angels and Demons");
        book.setYear(2000);
        book.setIsbn("0-671-02735-2");

        List<Book> books = author.getBooks();
        int beforeSize = books.size();
        books.add(book);

        //do a put now to update the author info
        res.put(author);

        //check if the books size got updated for this author
        Author author1 = res.get(Author.class);
        assertNotNull(author1);
        assertTrue(author1.getBooks().size() > beforeSize );

    }

    /**
     * delete the book created in {@link #testCreateAuthorWithPostXml }
     *
     * @throws Exception
     */

    @Test
    public void testDeleteAuthor() throws Exception {

        String fname = "J.R.R", lname = "Tolkien";

        //get the author
        WebResource res = resource.path("authors").path("query").queryParam("firstName", fname).queryParam("lastName", lname);
        System.out.println("Using resource uri: " + res.toString());
        Author author = res.get(Author.class);

        assertNotNull(author);
        assertTrue(author.getLastName().equals(lname));

        String authorId = author.getId();

        //delete the author
        WebResource res2 = resource.path("authors").path(authorId);
        res2.delete();

        //do a get for this author to check there is no such resource
        ClientResponse clientResponse = res2.get(ClientResponse.class);
        ClientResponse.Status status = clientResponse.getClientResponseStatus();

        assertEquals(status, ClientResponse.Status.NOT_FOUND);

    }

    @Test
    public void testGetFile() throws Exception {

        String fileName = "/schema.xsd";
        InputStream is = this.getClass().getResourceAsStream(fileName);

        assertNotNull(is);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        while (s!=null) {
            sb.append(s).append("\n");
            s = br.readLine();
        }

        WebResource res = resource.path("authors/file").path(fileName);
        String fileContents = res.get(String.class);

        assertNotNull(fileContents);
        assertTrue(fileContents.length() > 0);
        assertEquals(sb.toString(), fileContents);

    }
}
