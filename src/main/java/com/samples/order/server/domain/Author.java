package com.softwareag.mediator.server.domain;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mgunasek
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
@XmlType(name = "AuthorType",
         propOrder = {"firstName", "middleName", "lastName", "address", "books" })
public class Author {

    @XmlID
    @XmlAttribute
    private String id;  //should be string type for @XmlID

    @XmlElement(required = true)
    private String firstName;
    
    @XmlElement(required = true)
    private String lastName;
    
    private String middleName;

    @XmlElement(required = true)
    private Address address;

    @XmlElementWrapper(name = "books")
    @XmlElements(@XmlElement(name = "book", type = Book.class))
    private List<Book> books;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        if (books == null) {
            books = new ArrayList<Book>();
        }
        return books;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
