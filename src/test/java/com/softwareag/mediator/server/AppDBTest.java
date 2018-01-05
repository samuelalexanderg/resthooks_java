package com.softwareag.mediator.server;

import com.softwareag.mediator.server.domain.Author;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author mgunasek
 */
public class AppDBTest {

    @Test
    public void testGetAuthors() throws Exception {
        List<Author> authorList = AppDB.get().getAuthorsFromFile();
        assertNotNull(authorList);
        assertTrue(authorList.size() > 0); 
    }
}
