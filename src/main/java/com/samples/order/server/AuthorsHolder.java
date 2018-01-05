package com.softwareag.mediator.server;

import com.softwareag.mediator.server.domain.Author;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
* @author mgunasek
*/
@XmlType(name = "")
@XmlRootElement(name = "authors")
public class AuthorsHolder {

    @XmlElement(name = "author")
    List<Author> authors;

    public AuthorsHolder() {
        authors = new ArrayList<Author>();
    }

    public List<Author> getAuthors() {
        return authors;
    }
}
