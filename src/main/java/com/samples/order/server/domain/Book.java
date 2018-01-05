package com.softwareag.mediator.server.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author mgunasek
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BookType",
         propOrder = {"title", "year", "isbn"})
public class Book {

    @XmlElement(required = true)
    private String title;

    private int year;

    @XmlElement(required = true)
    private String isbn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
